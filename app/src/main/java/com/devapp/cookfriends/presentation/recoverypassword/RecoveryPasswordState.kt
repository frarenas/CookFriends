package com.devapp.cookfriends.presentation.recoverypassword

import com.devapp.cookfriends.domain.model.RecoveryStep
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText

data class RecoveryPasswordState(
    val isLoading: Boolean = false,
    val message: UiMessage? = null,
    val currentStep: RecoveryStep = RecoveryStep.EnterEmail,
    val usernameErrorMessage: UiText? = null,
    val recoveryCodeErrorMessage: UiText? = null,
    val passwordErrorMessage: UiText? = null,
    val repeatPasswordErrorMessage: UiText? = null,

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
    val isUploadingRecipe: Boolean = false,
)
