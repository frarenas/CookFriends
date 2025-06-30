package com.devapp.cookfriends.presentation.editrecipe

import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText

data class EditRecipeState(
    val recipe: Recipe = Recipe(),
    val isLoading: Boolean = false,
    val message: UiMessage? = null,
    val nameErrorMessage: UiText? = null,
    val descriptionErrorMessage: UiText? = null,
    val recipePhotoErrorMessage: UiText? = null,
    val recipePhotosErrorMessage: UiText? = null,
    val portionsErrorMessage: UiText? = null,
    val ingredientsErrorMessage: UiText? = null,
    val stepsErrorMessage: UiText? = null,
    val recipeTypeErrorMessage: UiText? = null,
    val ingredientNameErrorMessage: UiText? = null,
    val ingredientQuantityErrorMessage: UiText? = null,
    val stepContentErrorMessage: UiText? = null,
)
