// En tu paquete de viewmodel, ej: com.devapp.cookfriends.presentation.recoverypassword
package com.devapp.cookfriends.presentation.recoverypassword

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.model.RecoveryStep // Asegúrate de importar tu RecoveryStep
import com.devapp.cookfriends.domain.usecase.ChangePasswordByUsernameUseCase
import com.devapp.cookfriends.domain.usecase.GetUsernameUseCase
import com.devapp.cookfriends.presentation.profile.ProfileNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

// Asume que tienes un repositorio o caso de uso para interactuar con el backend
// interface AuthRepository {
//     suspend fun requestPasswordRecoveryCode(email: String): Result<Unit> // Result podría ser una clase sellada propia para manejar éxito/error
//     suspend fun validateRecoveryCode(email: String, code: String): Result<Unit>
//     suspend fun resetPassword(email: String, code: String, newPassword: String): Result<Unit>
// }

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
        _errorMessage.value = null // Limpiar error al escribir
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
        println("submitUserName() llamado")
        if (email.value.isBlank()) {
            _errorMessage.value = "El nombre de usuario no puede estar vacío."
            return
        }
        _isLoading.value = true
        println("Antes del launch")
        viewModelScope.launch {
            println("Quiere lanzar api con profile:")
            // --- LÓGICA DE BACKEND (SIMULADA) ---
            delay(1500) // Simular llamada de red
            val user  = getUsernameUseCase(email.value)
            println("Se ejecuto el endpoint, $user")
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

                if (response?.status == "SUCCESS") {
                    _errorMessage.value = "Contraseña cambiada con éxito."
                    _navigationEvent.emit(ProfileNavigationEvent.NavigateToLogin)
                } else {
                    _errorMessage.value = response?.message ?: "Error al cambiar la contraseña."
                }

            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}