package com.devapp.cookfriends.presentation.recipeDetail

import androidx.lifecycle.ViewModel
import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _recipeDetailState = MutableStateFlow(RecipeDetailState())
    val recipeDetailState: StateFlow<RecipeDetailState> = _recipeDetailState

}