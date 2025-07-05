package com.devapp.cookfriends.presentation.profile

import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText

data class ProfileState(
    val message: UiMessage? = null,
    val passwordErrorMessage: UiText? = null,
    val repeatPasswordErrorMessage: UiText? = null,
    val isChangingPassword: Boolean = false,
    val isLoggingOut: Boolean = false
)
