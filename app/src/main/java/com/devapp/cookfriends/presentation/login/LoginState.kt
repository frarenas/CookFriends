package com.devapp.cookfriends.presentation.login

import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.model.User

data class LoginState(
    val user: User? = null,
    val error: UiMessage? = null,
    val usernameErrorMessage: UiText? = null,
    val passwordErrorMessage: UiText? = null,
    val isLoggingIn: Boolean = false,
    val isLoggingInGuest: Boolean = false
)
