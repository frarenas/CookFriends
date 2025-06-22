package com.devapp.cookfriends.presentation.editrecipe

import com.devapp.cookfriends.domain.model.Recipe

data class EditRecipeState(
    val recipe: Recipe = Recipe(),
    val isLoading: Boolean = false,
    val error: String? = null
)
