package com.devapp.cookfriends.presentation.profile

import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText

data class ProfileState(
    val isLoading: Boolean = false,
    val loggedOut: Boolean = false,
    val message: UiMessage? = null,
    val passwordErrorMessage: UiText? = null,
    val repeatPasswordErrorMessage: UiText? = null
)
