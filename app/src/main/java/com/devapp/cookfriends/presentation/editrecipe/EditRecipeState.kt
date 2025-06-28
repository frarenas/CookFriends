package com.devapp.cookfriends.presentation.editrecipe

import com.devapp.cookfriends.domain.model.Recipe

data class EditRecipeState(
    val recipe: Recipe = Recipe(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val nameErrorMessage: String? = null,
    val descriptionErrorMessage: String? = null,
    val recipePhotoErrorMessage: String? = null,
    val recipePhotosErrorMessage: String? = null,
    val portionsErrorMessage: String? = null,
    val ingredientsErrorMessage: String? = null,
    val stepsErrorMessage: String? = null,
    val recipeTypeErrorMessage: String? = null,
    val ingredientNameErrorMessage: String? = null,
    val ingredientQuantityErrorMessage: String? = null,
    val stepContentErrorMessage: String? = null,
)
