package com.devapp.cookfriends.domain.model

data class UiError(
    val uiText: UiText,
    val blocking: Boolean = false
)
