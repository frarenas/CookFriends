package com.devapp.cookfriends.presentation.login

import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.model.User

data class LoginState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: UiMessage? = null,
    val continueToHome: Boolean = false,
    val usernameErrorMessage: UiText? = null,
    val passwordErrorMessage: UiText? = null
)
