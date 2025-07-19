package com.devapp.cookfriends.presentation.home.myrecipes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.RecipeType
import com.devapp.cookfriends.domain.model.SearchOptions
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.DeleteRecipeUseCase
import com.devapp.cookfriends.domain.usecase.GetLoggedUserIdUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeTypesUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipesUseCase
import com.devapp.cookfriends.domain.usecase.ToggleFavoriteUseCase
import com.devapp.cookfriends.presentation.home.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getRecipeTypesUseCase: GetRecipeTypesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getLoggedUserIdUseCase: GetLoggedUserIdUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {

    private val _recipesState = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState> = _recipesState

    private val _availableRecipeTypes = MutableStateFlow<List<RecipeType>>(emptyList())
    val availableRecipeTypes: StateFlow<List<RecipeType>> = _availableRecipeTypes.asStateFlow()

    private val _showSearchOptionsDialog = MutableStateFlow(false)
    val showSearchOptionsDialog: StateFlow<Boolean> = _showSearchOptionsDialog.asStateFlow()

    private val _currentSearchOptions = MutableStateFlow(SearchOptions())
    val currentSearchOptions: StateFlow<SearchOptions> = _currentSearchOptions.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab

    private var recipesJob: Job? = null

    init {
        searchRecipes()
    }

    fun searchRecipes() {
        viewModelScope.launch {
            recipesJob?.cancel()
            val loggedUserId = getLoggedUserIdUseCase()
            val updatedOptions = _currentSearchOptions.value.copy(
                currentUserId = loggedUserId,
                userCalculated = selectedTab.value == 1
            )
            _currentSearchOptions.value = updatedOptions
            recipesJob = getRecipesUseCase(updatedOptions)
                .onStart {
                    _recipesState.update { it.copy(isLoading = true) }
                }
                .catch { exception ->
                    _recipesState.update {
                        it.copy(
                            isLoading = false,
                            message = UiMessage(
                                UiText.StringResource(R.string.generic_error),
                                blocking = true
                            )
                        )
                    }
                }
                .onEach { recipes ->
                    _recipesState.update {
                        it.copy(
                            recipeList = recipes,
                            isLoading = false
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun deleteRecipe(recipeId: Uuid) {
        viewModelScope.launch {
            try {
                deleteRecipeUseCase(recipeId)
                _recipesState.update {
                    it.copy(
                        message = UiMessage(
                            UiText.StringResource(R.string.recipe_deleted_successfully),
                            blocking = false
                        )
                    )
                }
            } catch (e: Exception) {
                _recipesState.update {
                    it.copy(
                        isLoading = false,
                        message = UiMessage(
                            uiText = if (e.message != null) UiText.DynamicString(
                                e.message ?: ""
                            ) else UiText.StringResource(R.string.generic_error),
                            blocking = false
                        )
                    )
                }
                Log.e("MyRecipesViewModel", "Error deleting recipe", e)
            }
        }
    }

    fun getAvailableRecipeTypes() {
        viewModelScope.launch {
            getRecipeTypesUseCase()
                .onEach { recipeType ->
                    _availableRecipeTypes.value = recipeType
                }
                .launchIn(this)
        }
    }

    fun applySearchOptions(options: SearchOptions) {
        val newOptions = options.copy(
            currentUserId = _currentSearchOptions.value.currentUserId,
            userCalculated = _selectedTab.value == 1
        )
        _currentSearchOptions.value = newOptions
        dismissSearchOptionsDialog()
        searchRecipes()

    }

    fun openSearchOptionsDialog() {
        _showSearchOptionsDialog.value = true
    }

    fun dismissSearchOptionsDialog() {
        _showSearchOptionsDialog.value = false
    }

    fun toggleFavorite(recipeId: Uuid) {
        viewModelScope.launch {
            toggleFavoriteUseCase(recipeId)
        }
    }

    fun setSelectedTab(index: Int) {
        if (_selectedTab.value == index) return
        _selectedTab.value = index
        searchRecipes()
    }

    fun onClearMessage() {
        _recipesState.update { it.copy(message = null) }
    }
}
