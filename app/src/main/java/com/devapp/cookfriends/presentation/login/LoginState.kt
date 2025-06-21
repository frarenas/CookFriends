package com.devapp.cookfriends.presentation.login

import com.devapp.cookfriends.domain.model.User

data class LoginState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
