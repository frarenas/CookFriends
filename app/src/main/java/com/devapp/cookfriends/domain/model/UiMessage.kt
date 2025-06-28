package com.devapp.cookfriends.domain.model

data class UiMessage(
    val uiText: UiText,
    val blocking: Boolean = false
)
