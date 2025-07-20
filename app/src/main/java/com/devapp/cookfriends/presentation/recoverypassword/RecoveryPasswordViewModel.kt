package com.devapp.cookfriends.presentation.recoverypassword

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.RecoveryStep
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.ChangePasswordByUsernameUseCase
import com.devapp.cookfriends.domain.usecase.GetUsernameUseCase
import com.devapp.cookfriends.util.ConnectivityObserver
import com.devapp.cookfriends.util.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
open class RecoveryPasswordViewModel @Inject constructor(
    private val getUsernameUseCase: GetUsernameUseCase,
    private val changePasswordByUsernameUseCase: ChangePasswordByUsernameUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _recoveryPasswordState = MutableStateFlow(RecoveryPasswordState())
    val recoveryPasswordState: StateFlow<RecoveryPasswordState> = _recoveryPasswordState

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _code = mutableStateOf("")
    val code: State<String> = _code

    private val _newPassword = mutableStateOf("")
    val newPassword: State<String> = _newPassword

    private val _repeatPassword = mutableStateOf("")
    val repeatPassword: State<String> = _repeatPassword

    private val _showConfirmationDialog = mutableStateOf(false)
    val showConfirmationDialog: State<Boolean> = _showConfirmationDialog

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _recoveryPasswordState.update { it.copy(usernameErrorMessage = null) }
    }

    fun onCodeChange(newCode: String) {
        _code.value = newCode
        _recoveryPasswordState.update { it.copy(recoveryCodeErrorMessage = null) }
    }

    fun onPasswordChange(newPass: String) {
        _newPassword.value = newPass
        _recoveryPasswordState.update { it.copy(passwordErrorMessage = null) }
    }

    fun onConfirmPasswordChange(newConfirmPass: String) {
        _repeatPassword.value = newConfirmPass
        _recoveryPasswordState.update { it.copy(repeatPasswordErrorMessage = null) }
    }

    fun submitUsername() {
        val isUsernameValid = validateUsername()
        if (isUsernameValid) {
            viewModelScope.launch {
                try {
                    if (connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Unavailable) {
                        _recoveryPasswordState.update {
                            it.copy(
                                message = UiMessage(
                                    UiText.StringResource(R.string.no_internet_connection),
                                    blocking = false
                                )
                            )
                        }
                    } else {
                        _recoveryPasswordState.update { it.copy(isLoading = true) }
                        val user = getUsernameUseCase(email.value.trim().lowercase())
                        if (user != null) {
                            _recoveryPasswordState.update { it.copy(currentStep = RecoveryStep.EnterCode) }
                        } else {
                            _recoveryPasswordState.update {
                                it.copy(
                                    isLoading = false,
                                    message = UiMessage(
                                        uiText = UiText.StringResource(R.string.user_not_found),
                                        blocking = false
                                    )
                                )
                            }
                        }
                        _recoveryPasswordState.update { it.copy(isLoading = false) }
                    }
                } catch (e: Exception) {
                    _recoveryPasswordState.update {
                        it.copy(
                            isLoading = false,
                            message = UiMessage(
                                uiText = if (e.message != null) UiText.DynamicString(
                                    e.message ?: ""
                                ) else UiText.StringResource(R.string.generic_error),
                                blocking = false
                            )
                        )
                    }
                    Log.e("RecoveryPasswordViewModel", "Error al obtener el usuario", e)
                }

            }
        }
    }

    fun submitCode() {
        val isVerificationCodeValid = validateVerificationCode()
        if (isVerificationCodeValid) {
            if (code.value != "123456") {
                _recoveryPasswordState.update {
                    it.copy(
                        isLoading = false,
                        message = UiMessage(
                            uiText = UiText.StringResource(R.string.recovery_code_incorrect),
                            blocking = false
                        )
                    )
                }
                return
            }
            _recoveryPasswordState.update { it.copy(currentStep = RecoveryStep.EnterNewPassword) }
        }
    }

    fun submitNewPassword() {
        val isChangePasswordValid = validateChangePassword()
        if (isChangePasswordValid) {
            viewModelScope.launch {
                try {
                    if (connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Unavailable) {
                        _recoveryPasswordState.update {
                            it.copy(
                                message = UiMessage(
                                    UiText.StringResource(R.string.no_internet_connection),
                                    blocking = false
                                )
                            )
                        }
                    } else {
                        _recoveryPasswordState.update { it.copy(isLoading = true) }
                        changePasswordByUsernameUseCase(
                            username = email.value.trim().lowercase(),
                            newPassword = _newPassword.value
                        )
                        _showConfirmationDialog.value = true
                    }
                } catch (e: Exception) {
                    _recoveryPasswordState.update {
                        it.copy(
                            isLoading = false,
                            message = UiMessage(
                                uiText = if (e.message != null) UiText.DynamicString(
                                    e.message ?: ""
                                ) else UiText.StringResource(R.string.generic_error),
                                blocking = false
                            )
                        )
                    }
                    Log.e("RecoveryPasswordViewModel", "Error al cambiar la contraseña", e)
                }
            }
        }
    }

    fun onClearMessage() {
        _recoveryPasswordState.update { it.copy(message = null) }
    }

    private fun validateUsername(): Boolean {
        var isValid = true
        if (_email.value.isBlank()) {
            _recoveryPasswordState.update {
                it.copy(
                    usernameErrorMessage = UiText.StringResource(R.string.username_mandatory)
                )
            }
            isValid = false
        }
        return isValid
    }

    private fun validateVerificationCode(): Boolean {
        var isValid = true
        if (_code.value.isBlank()) {
            _recoveryPasswordState.update {
                it.copy(
                    recoveryCodeErrorMessage = UiText.StringResource(R.string.verification_code_mandatory)
                )
            }
            isValid = false
        }
        return isValid
    }

    private fun validateChangePassword(): Boolean {
        var isValid = true
        if (_newPassword.value.isBlank()) {
            _recoveryPasswordState.update {
                it.copy(
                    passwordErrorMessage = UiText.StringResource(R.string.password_mandatory)
                )
            }
            isValid = false
        } else if (_newPassword.value.length < 8) {
            _recoveryPasswordState.update {
                it.copy(
                    passwordErrorMessage = UiText.StringResource(R.string.password_invalid)
                )
            }
            isValid = false
        } else {
            _recoveryPasswordState.update {
                it.copy(
                    passwordErrorMessage = null
                )
            }
        }

        if (_repeatPassword.value.isBlank()) {
            _recoveryPasswordState.update {
                it.copy(
                    repeatPasswordErrorMessage = UiText.StringResource(R.string.repeat_password_mandatory)
                )
            }
            isValid = false
        } else if (_repeatPassword.value != _newPassword.value) {
            _recoveryPasswordState.update {
                it.copy(
                    repeatPasswordErrorMessage = UiText.StringResource(R.string.passwords_does_not_match)
                )
            }
            isValid = false
        } else {
            _recoveryPasswordState.update {
                it.copy(
                    repeatPasswordErrorMessage = null
                )
            }
        }

        return isValid
    }
}
