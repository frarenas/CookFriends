package com.devapp.cookfriends.presentation.common

import androidx.compose.ui.graphics.Color

sealed class SnackbarMessage(val message: String, val backgroundColor: Color) {
    data class Success(val text: String) : SnackbarMessage(text, Color.Green)
    data class Error(val text: String) : SnackbarMessage(text, Color.Red)
}
