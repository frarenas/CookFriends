package com.devapp.cookfriends.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.UiError
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.LoginUseCase
import com.devapp.cookfriends.domain.usecase.LogoutUseCase
import com.devapp.cookfriends.util.ConnectivityObserver
import com.devapp.cookfriends.util.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _keepMeLoggedInChecked = MutableStateFlow<Boolean>(false)
    val keepMeLoggedInChecked: StateFlow<Boolean> = _keepMeLoggedInChecked

    fun login() {
        val isLoginValid = validateLogin()
        if (isLoginValid) {
            viewModelScope.launch {
                if (connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Unavailable) {
                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            error = UiError(
                                UiText.StringResource(R.string.no_internet_connection),
                                blocking = false
                            )
                        )
                    }
                } else {
                    _loginState.update { it.copy(isLoading = true) }
                    try {
                        loginUseCase(_username.value, _password.value, _keepMeLoggedInChecked.value)
                        _loginState.update { it.copy(continueToHome = true) }
                    } catch (e: Exception) {
                        _loginState.update {
                            it.copy(
                                isLoading = false,
                                error = UiError(
                                    uiText = if (e.message != null) UiText.DynamicString(
                                        e.message ?: ""
                                    ) else UiText.StringResource(R.string.generic_error),
                                    blocking = false
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun guestLogin() {
        viewModelScope.launch {
            try {
                _loginState.update { it.copy(isLoading = true) }
                logoutUseCase()
                _loginState.update { it.copy(continueToHome = true) }
            } catch (_: Exception) {
                _loginState.update { it.copy(isLoading = false) }
            }

        }
    }

    fun onUsernameChange(username: String) {
        _username.update { username }
    }

    fun onPasswordChange(password: String) {
        _password.update { password }
    }

    fun onKeepMeLoggedInCheckedChange(keepMeLoggedInChecked: Boolean) {
        _keepMeLoggedInChecked.update { keepMeLoggedInChecked }
    }

    fun onClearError() {
        _loginState.update { it.copy(error = null) }
    }

    private fun validateLogin(): Boolean {
        var isValid = true
        if (_username.value.isBlank()) {
            _loginState.update {
                it.copy(
                    usernameErrorMessage = UiText.StringResource(R.string.username_mandatory)
                )
            }
            isValid = false
        } else {
            _loginState.update {
                it.copy(
                    usernameErrorMessage = null
                )
            }
        }

        if (_password.value.isBlank()) {
            _loginState.update {
                it.copy(
                    passwordErrorMessage = UiText.StringResource(R.string.password_mandatory)
                )
            }
            isValid = false
        } else if (_password.value.length < 8) {
            _loginState.update {
                it.copy(
                    passwordErrorMessage = UiText.StringResource(R.string.password_invalid)
                )
            }
            isValid = false
        } else {
            _loginState.update {
                it.copy(
                    passwordErrorMessage = null
                )
            }
        }

        return isValid
    }
}
