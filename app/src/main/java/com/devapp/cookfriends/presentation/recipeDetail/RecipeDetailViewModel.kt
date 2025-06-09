package com.devapp.cookfriends.presentation.recipeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import com.devapp.cookfriends.domain.models.toDomain
import com.devapp.cookfriends.presentation.home.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _recipeDetailState = MutableStateFlow(RecipeDetailState())
    val recipeDetailState: StateFlow<RecipeDetailState> = _recipeDetailState

}