package com.devapp.cookfriends.presentation.home

import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.UiError

data class RecipesState(
    val recipeList: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiError? = null
)
