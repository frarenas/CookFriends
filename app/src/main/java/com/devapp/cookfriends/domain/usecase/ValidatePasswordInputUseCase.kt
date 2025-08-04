package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.UiText
import javax.inject.Inject

data class PasswordValidationResult(
    val newPasswordError: UiText? = null,
    val repeatPasswordError: UiText? = null
) {
    val isValid: Boolean
        get() = newPasswordError == null && repeatPasswordError == null
}

class ValidatePasswordInputUseCase @Inject constructor() { // No dependencies needed for this simple validation

    operator fun invoke(newPassword: String, repeatPassword: String): PasswordValidationResult {
        val newPasswordError = when {
            newPassword.isBlank() -> UiText.StringResource(R.string.password_mandatory)
            newPassword.length < 8 -> UiText.StringResource(R.string.password_invalid)
            else -> null
        }

        val repeatPasswordError = when {
            repeatPassword.isBlank() -> UiText.StringResource(R.string.repeat_password_mandatory)
            repeatPassword != newPassword -> UiText.StringResource(R.string.passwords_does_not_match)
            else -> null
        }

        return PasswordValidationResult(
            newPasswordError = newPasswordError,
            repeatPasswordError = repeatPasswordError
        )
    }
}
