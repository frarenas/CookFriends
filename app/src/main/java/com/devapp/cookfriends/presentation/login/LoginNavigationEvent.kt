package com.devapp.cookfriends.presentation.login

sealed class LoginNavigationEvent {
    object NavigateToHome : LoginNavigationEvent()
}