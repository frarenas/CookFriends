package com.devapp.cookfriends.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.ChangePasswordUseCase
import com.devapp.cookfriends.domain.usecase.LogoutUseCase
import com.devapp.cookfriends.util.ConnectivityObserver
import com.devapp.cookfriends.util.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _repeatPassword = MutableStateFlow("")
    val repeatPassword: StateFlow<String> = _repeatPassword

    private val _navigationEvent = MutableSharedFlow<ProfileNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun updatePassword() {
        val isChangePasswordValid = validateChangePassword()
        if (isChangePasswordValid) {
            viewModelScope.launch {
                if (connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Unavailable) {
                    _profileState.update {
                        it.copy(
                            message = UiMessage(
                                UiText.StringResource(R.string.no_internet_connection),
                                blocking = false
                            )
                        )
                    }
                } else {
                    _profileState.update { it.copy(isChangingPassword = true) }
                    try {
                        changePasswordUseCase(_newPassword.value)
                        _profileState.update {
                            it.copy(
                                isChangingPassword = false,
                                message = UiMessage(
                                    uiText = UiText.StringResource(R.string.password_was_updated),
                                    blocking = false
                                )
                            )
                        }
                        onNewPasswordChange("")
                        onRepeatPasswordChange("")
                    } catch (e: Exception) {
                        _profileState.update {
                            it.copy(
                                isChangingPassword = false,
                                message = UiMessage(
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

    fun logout() {
        viewModelScope.launch {
            try {
                _profileState.update { it.copy(isLoggingOut = true) }
                logoutUseCase()
                _navigationEvent.emit(ProfileNavigationEvent.NavigateToLogin)
            } catch (_: Exception) {
                _profileState.update { it.copy(isLoggingOut = false) }
            }

        }
    }

    fun onNewPasswordChange(password: String) {
        _newPassword.update { password }
    }

    fun onRepeatPasswordChange(password: String) {
        _repeatPassword.update { password }
    }

    fun onClearMessage() {
        _profileState.update { it.copy(message = null) }
    }

    private fun validateChangePassword(): Boolean {
        var isValid = true
        if (_newPassword.value.isBlank()) {
            _profileState.update {
                it.copy(
                    passwordErrorMessage = UiText.StringResource(R.string.password_mandatory)
                )
            }
            isValid = false
        } else if (_newPassword.value.length < 8) {
            _profileState.update {
                it.copy(
                    passwordErrorMessage = UiText.StringResource(R.string.password_invalid)
                )
            }
            isValid = false
        } else {
            _profileState.update {
                it.copy(
                    passwordErrorMessage = null
                )
            }
        }

        if (_repeatPassword.value.isBlank()) {
            _profileState.update {
                it.copy(
                    repeatPasswordErrorMessage = UiText.StringResource(R.string.repeat_password_mandatory)
                )
            }
            isValid = false
        } else if (_repeatPassword.value != _newPassword.value) {
            _profileState.update {
                it.copy(
                    repeatPasswordErrorMessage = UiText.StringResource(R.string.passwords_does_not_match)
                )
            }
            isValid = false
        } else {
            _profileState.update {
                it.copy(
                    repeatPasswordErrorMessage = null
                )
            }
        }

        return isValid
    }
}
