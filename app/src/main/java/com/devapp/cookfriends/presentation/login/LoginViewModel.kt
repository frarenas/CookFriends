package com.devapp.cookfriends.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.usecase.LoginUseCase
import com.devapp.cookfriends.presentation.common.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
): ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    private val _snackbarFlow = MutableSharedFlow<SnackbarMessage>()
    val snackbarFlow: SharedFlow<SnackbarMessage> = _snackbarFlow

    fun login(username: String, password: String, keepMeLoggedIn: Boolean) {
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true) }
            try {
                val user = loginUseCase(username, password, keepMeLoggedIn)
                _loginState.update { it.copy(user = user, isLoading = false) }
            } catch (e: Exception) {
                _snackbarFlow.emit(SnackbarMessage.Error(e.message ?: "Se produjo un error."))
                _loginState.update { it.copy(isLoading = false) }
            }
        }
    }
}
