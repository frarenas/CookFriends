package com.devapp.cookfriends.presentation.home.recipes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.RecipeType
import com.devapp.cookfriends.domain.model.SearchOptions
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.GetRecipeTypesUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipesUseCase
import com.devapp.cookfriends.domain.usecase.ToggleFavoriteUseCase
import com.devapp.cookfriends.presentation.home.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getRecipeTypesUseCase: GetRecipeTypesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _recipesState = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState> = _recipesState

    private val _availableRecipeTypes = MutableStateFlow<List<RecipeType>>(emptyList())
    val availableRecipeTypes: StateFlow<List<RecipeType>> = _availableRecipeTypes.asStateFlow()

    private val _showSearchOptionsDialog = MutableStateFlow(false)
    val showSearchOptionsDialog: StateFlow<Boolean> = _showSearchOptionsDialog.asStateFlow()

    private val _currentSearchOptions = MutableStateFlow(SearchOptions())
    val currentSearchOptions: StateFlow<SearchOptions> = _currentSearchOptions.asStateFlow()

    init {
        searchRecipes(_currentSearchOptions.value)
        getAvailableRecipeTypes()
    }

    fun searchRecipes(options: SearchOptions) {
        viewModelScope.launch {
            _recipesState.update { it.copy(isLoading = true, message = null) }
            getRecipesUseCase(options)
                .onStart {
                    _recipesState.update { it.copy(isLoading = true, message = null) }
                }
                .catch { exception ->
                    _recipesState.update {
                        it.copy(
                            isLoading = false,
                            message = UiMessage(
                                UiText.StringResource(R.string.error_fetching_recipes),
                                blocking = true
                            )
                        )
                    }
                    Log.e("RecipesViewModel", "Error searching recipes", exception)
                }
                .collect { recipes ->
                    _recipesState.update {
                        it.copy(
                            recipeList = recipes,
                            isLoading = false,
                            message = null
                        )
                    }
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
        _currentSearchOptions.value = options
        dismissSearchOptionsDialog()

        searchRecipes(options)
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
}
