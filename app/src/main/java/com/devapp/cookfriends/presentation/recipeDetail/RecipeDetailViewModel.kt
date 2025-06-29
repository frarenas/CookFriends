package com.devapp.cookfriends.presentation.recipeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import com.devapp.cookfriends.domain.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipeDetailState = MutableStateFlow(RecipeDetailState())
    val recipeDetailState: StateFlow<RecipeDetailState> = _recipeDetailState

    fun loadRecipe(id: Uuid) {
        viewModelScope.launch {
            try {
                val recipe = recipeRepository.getRecipeById(id)
                _recipeDetailState.value = _recipeDetailState.value.copy(recipe = recipe)
            } catch (e: Exception) {
                // Podés loguear o actualizar un campo de error
            }
        }
    }
}
