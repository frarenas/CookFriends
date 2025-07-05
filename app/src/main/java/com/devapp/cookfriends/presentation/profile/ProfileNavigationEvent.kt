package com.devapp.cookfriends.presentation.profile

sealed class ProfileNavigationEvent {
    object NavigateToLogin : ProfileNavigationEvent()
}