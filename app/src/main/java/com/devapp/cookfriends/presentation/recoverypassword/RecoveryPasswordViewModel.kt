package com.devapp.cookfriends.presentation.recoverypassword

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.model.RecoveryStep
import com.devapp.cookfriends.domain.usecase.ChangePasswordByUsernameUseCase
import com.devapp.cookfriends.domain.usecase.GetUsernameUseCase
import com.devapp.cookfriends.presentation.profile.ProfileNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
open class RecoveryPasswordViewModel @Inject constructor(
    private val getUsernameUseCase: GetUsernameUseCase,
    private val changePasswordByUsernameUseCase: ChangePasswordByUsernameUseCase
) : ViewModel() {

    private val _currentStep = mutableStateOf<RecoveryStep>(RecoveryStep.EnterEmail)
    val currentStep: State<RecoveryStep> = _currentStep

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _code = mutableStateOf("")
    val code: State<String> = _code

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: State<String> = _confirmPassword

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _navigationEvent = MutableSharedFlow<ProfileNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _errorMessage.value = null
    }

    fun onCodeChange(newCode: String) {
        _code.value = newCode
        _errorMessage.value = null
    }

    fun onPasswordChange(newPass: String) {
        _password.value = newPass
        _errorMessage.value = null
    }

    fun onConfirmPasswordChange(newConfirmPass: String) {
        _confirmPassword.value = newConfirmPass
        _errorMessage.value = null
    }

    fun submitUsername() {
        if (email.value.isBlank()) {
            _errorMessage.value = "El nombre de usuario no puede estar vacío."
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            val user  = getUsernameUseCase(email.value.trim().lowercase())
            if (user != null) {
                _currentStep.value = RecoveryStep.EnterCode
                _errorMessage.value = null
            } else {
                _errorMessage.value = "No se encontró un usuario con ese nombre de usuario."
            }
            _isLoading.value = false
        }
    }

    fun submitCode() {
        println("submitCode() llamado")
        if (code.value != "123456") {
            _errorMessage.value = "El código ingresado es incorrecto."
            return
        }
        _currentStep.value = RecoveryStep.EnterNewPassword
    }


    fun submitNewPassword() {
        println("submitNewPassword() llamado")
        if (password.value.length < 8) {
            _errorMessage.value = "La contraseña debe tener al menos 8 caracteres."
            return
        }
        if (password.value != confirmPassword.value) {
            _errorMessage.value = "Las contraseñas no coinciden."
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = changePasswordByUsernameUseCase(
                    username = email.value,
                    newPassword = password.value
                )

                if (response.status == "SUCCESS") {
                    _errorMessage.value = "Contraseña cambiada con éxito."
                    _navigationEvent.emit(ProfileNavigationEvent.NavigateToLogin)
                } else {
                    _errorMessage.value = response.message ?: "Error al cambiar la contraseña."
                }

            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
