package com.devapp.cookfriends.presentation.recipe

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Comment
import com.devapp.cookfriends.domain.model.Rating
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.GetLoggedUserIdUseCase
import com.devapp.cookfriends.domain.usecase.GetLoggedUserUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import com.devapp.cookfriends.domain.usecase.IsUserLoggedUseCase
import com.devapp.cookfriends.domain.usecase.ToggleFavoriteUseCase
import com.devapp.cookfriends.domain.usecase.AddCommentUseCase
import com.devapp.cookfriends.domain.usecase.RateRecipeUseCase
import com.devapp.cookfriends.presentation.navigation.EditRecipe
import com.devapp.cookfriends.presentation.navigation.UuidNavType
import com.devapp.cookfriends.util.ConnectivityObserver
import com.devapp.cookfriends.util.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getLoggedUserIdUseCase: GetLoggedUserIdUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val rateRecipeUseCase: RateRecipeUseCase,
    private val connectivityObserver: ConnectivityObserver,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: EditRecipe = savedStateHandle.toRoute(
        typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)
    )
    private val recipeId: Uuid? = navArgs.id

    private val _recipeState = MutableStateFlow(RecipeState())
    val recipeState: StateFlow<RecipeState> = _recipeState

    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged: StateFlow<Boolean> = _isUserLogged

    private val _newComment = MutableStateFlow("")
    val newComment: StateFlow<String> = _newComment

    init {
        getRecipe()
        isUserLogged()
    }

    private fun getRecipe() {
        if (recipeId == null) {
            _recipeState.update {
                it.copy(
                    isLoading = false,
                    message = UiMessage(
                        uiText = UiText.StringResource(R.string.recipe_not_found),
                        blocking = true
                    )
                )
            }
        } else {
            viewModelScope.launch {
                _recipeState.update { it.copy(isLoading = true, message = null) }
                getRecipeUseCase(recipeId)
                    .onStart {
                        _recipeState.update { it.copy(isLoading = true, message = null) }
                    }
                    .catch { e ->
                        _recipeState.update {
                            it.copy(
                                isLoading = true,
                                message = UiMessage(
                                    uiText = if (e.message != null) UiText.DynamicString(
                                        e.message ?: ""
                                    ) else UiText.StringResource(R.string.generic_error),
                                    blocking = false
                                )
                            )
                        }
                    }
                    .collect { recipe ->
                        if (recipe == null) {
                            _recipeState.update {
                                it.copy(
                                    isLoading = false,
                                    message = UiMessage(
                                        uiText = UiText.StringResource(R.string.recipe_not_found),
                                        blocking = true
                                    )
                                )
                            }
                        } else {
                            val userId = getLoggedUserIdUseCase()
                            val comments =
                                recipe.comments.filter { it.user.id == userId || it.published }
                                    .sortedByDescending { it.date }
                            recipe.comments = comments
                            _recipeState.update {
                                it.copy(
                                    recipe = recipe,
                                    isLoading = false,
                                    message = null
                                )
                            }
                            _recipeState.update { it.copy(isEditable = it.recipe?.user?.id == userId) }
                        }
                    }
            }
        }
    }

    private fun isUserLogged() {
        viewModelScope.launch {
            _isUserLogged.value = isUserLoggedUseCase()
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _recipeState.value.recipe?.let {
                toggleFavoriteUseCase(it.id)
            }
        }
    }

    fun onNewCommentChange(comment: String) {
        _newComment.update { comment }
    }

    fun onClearMessage() {
        _recipeState.update { it.copy(message = null) }
    }

    fun rateRecipe(rate: Int) {
        viewModelScope.launch {
            if (connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Unavailable) {
                _recipeState.update {
                    it.copy(
                        message = UiMessage(
                            UiText.StringResource(R.string.no_internet_connection),
                            blocking = false
                        )
                    )
                }
            } else {
                _recipeState.update { it.copy(isSendingRating = true) }
                try {
                    val rating = Rating(
                        recipeId = _recipeState.value.recipe!!.id,
                        userId = getLoggedUserIdUseCase()!!,
                        rate = rate
                    )
                    rateRecipeUseCase(rating)
                    _recipeState.update {
                        it.copy(
                            isSendingRating = false,
                            message = UiMessage(
                                uiText = UiText.StringResource(R.string.comment_sent),
                                blocking = false
                            )
                        )
                    }
                } catch (e: Exception) {
                    _recipeState.update {
                        it.copy(
                            isSendingRating = false,
                            message = UiMessage(
                                uiText = UiText.StringResource(R.string.generic_error),
                                blocking = false
                            )
                        )
                    }
                    Log.e("RecipeViewModel", "Error sending rating", e)
                }
            }
        }
    }

    fun sendComment() {
        val isCommentValid = validateComment()
        if (isCommentValid) {
            viewModelScope.launch {
                if (connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Unavailable) {
                    _recipeState.update {
                        it.copy(
                            message = UiMessage(
                                UiText.StringResource(R.string.no_internet_connection),
                                blocking = false
                            )
                        )
                    }
                } else {
                    _recipeState.update { it.copy(isSendingComment = true) }
                    try {
                        val comment = Comment(
                            comment = _newComment.value,
                            recipeId = _recipeState.value.recipe!!.id,
                            user = getLoggedUserUseCase(),
                            date = kotlinx.datetime.Clock.System.now(),
                            updatePending = true
                        )
                        addCommentUseCase(comment)
                        _recipeState.update {
                            it.copy(
                                isSendingComment = false,
                                message = UiMessage(
                                    uiText = UiText.StringResource(R.string.comment_sent),
                                    blocking = false
                                )
                            )
                        }
                        onNewCommentChange("")
                    } catch (e: Exception) {
                        _recipeState.update {
                            it.copy(
                                isSendingComment = false,
                                message = UiMessage(
                                    uiText = if (e.message != null) UiText.DynamicString(
                                        e.message ?: ""
                                    ) else UiText.StringResource(R.string.generic_error),
                                    blocking = false
                                )
                            )
                        }
                        Log.e("RecipeViewModel", "Error sending comment", e)
                    }
                }
            }
        }
    }

    private fun validateComment(): Boolean {
        var isValid = true
        if (_newComment.value.isBlank()) {
            _recipeState.update {
                it.copy(
                    commentErrorMessage = UiText.StringResource(R.string.empty_comment_error)
                )
            }
            isValid = false
        } else {
            _recipeState.update {
                it.copy(
                    commentErrorMessage = null
                )
            }
        }

        return isValid
    }
}
