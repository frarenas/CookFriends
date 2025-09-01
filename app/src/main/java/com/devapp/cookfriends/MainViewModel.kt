package com.devapp.cookfriends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.usecase.IsSessionActiveUseCase
import com.devapp.cookfriends.presentation.navigation.Home
import com.devapp.cookfriends.presentation.navigation.Login
import com.devapp.cookfriends.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    isLoggedUseCase: IsSessionActiveUseCase
) : ViewModel() {
    val startDestination: StateFlow<Screen?> = isLoggedUseCase()
        .map { isActiveSession ->
            if (isActiveSession) Home else Login
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
