package com.devapp.cookfriends.presentation.home

import com.devapp.cookfriends.domain.models.Recipe

data class RecipesState(
    val recipeList: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
