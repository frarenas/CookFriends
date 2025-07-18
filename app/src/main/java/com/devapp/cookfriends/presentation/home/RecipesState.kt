package com.devapp.cookfriends.presentation.home

import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.UiMessage

data class RecipesState(
    val recipeList: List<Recipe> = emptyList(),
    val isLoading: Boolean = true,
    val message: UiMessage? = null
)
