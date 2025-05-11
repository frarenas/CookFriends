package com.devapp.cookfriends.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
): ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState

    init {
        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true, error = null) }
            try {
                val recipes = recipeRepository.getRecipes()
                _homeState.update { it.copy(recipeList= recipes, isLoading = false) }
            } catch (e: Exception) {
                _homeState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}