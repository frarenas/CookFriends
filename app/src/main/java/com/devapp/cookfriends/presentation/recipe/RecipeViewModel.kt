package com.devapp.cookfriends.presentation.recipe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.GetLoggedUserIdUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import com.devapp.cookfriends.domain.usecase.IsUserLoggedUseCase
import com.devapp.cookfriends.domain.usecase.ToggleFavoriteUseCase
import com.devapp.cookfriends.presentation.navigation.EditRecipe
import com.devapp.cookfriends.presentation.navigation.UuidNavType
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
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
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
                            _recipeState.update {
                                it.copy(
                                    recipe = recipe,
                                    isLoading = false,
                                    message  = null
                                )
                            }
                            _recipeState.update { it.copy(isEditable = it.recipe?.user?.id == getLoggedUserIdUseCase()) }
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
}
