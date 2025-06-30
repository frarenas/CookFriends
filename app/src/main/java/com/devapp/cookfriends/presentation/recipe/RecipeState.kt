package com.devapp.cookfriends.presentation.recipe

import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText

data class RecipeState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null,
    val isEditable: Boolean = false,
    val commentErrorMessage: UiText? = null
)
