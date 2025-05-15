package com.devapp.cookfriends.presentation.home.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.data.repository.RecipeRepository
import com.devapp.cookfriends.presentation.home.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
): ViewModel() {

    private val _recipesState = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState> = _recipesState

    init {
        viewModelScope.launch {
            _recipesState.update { it.copy(isLoading = true, error = null) }
            try {
                val recipes = recipeRepository.getRecipes()
                _recipesState.update { it.copy(recipeList= recipes, isLoading = false) }
            } catch (e: Exception) {
                _recipesState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
