package com.devapp.cookfriends.domain.model

data class UiMessage(
    val uiText: UiText,
    val blocking: Boolean = false,
    val action: (() -> Unit)? = null,
    val actionLabel: UiText? = null
)
