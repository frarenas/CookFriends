// En tu paquete de viewmodel, ej: com.devapp.cookfriends.presentation.recoverypassword
package com.devapp.cookfriends.presentation.recoverypassword

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.model.RecoveryStep // Asegúrate de importar tu RecoveryStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Asume que tienes un repositorio o caso de uso para interactuar con el backend
// interface AuthRepository {
//     suspend fun requestPasswordRecoveryCode(email: String): Result<Unit> // Result podría ser una clase sellada propia para manejar éxito/error
//     suspend fun validateRecoveryCode(email: String, code: String): Result<Unit>
//     suspend fun resetPassword(email: String, code: String, newPassword: String): Result<Unit>
// }

// @HiltViewModel // Si usas Hilt para inyección de dependencias
open class RecoveryPasswordViewModel(
    // private val authRepository: AuthRepository // Descomenta cuando tengas el repositorio
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

    fun submitEmail() {
        if (!isValidEmail(email.value)) {
            _errorMessage.value = "Por favor, ingresa un email válido."
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            // --- LÓGICA DE BACKEND (SIMULADA) ---
            delay(1500) // Simular llamada de red
            // val result = authRepository.requestPasswordRecoveryCode(email.value)
            // if (result.isSuccess) { // Asumiendo que tu Result tiene isSuccess
            _currentStep.value = RecoveryStep.EnterCode
            _isLoading.value = false
            _errorMessage.value = null
            // } else {
            //     _errorMessage.value = "Error al enviar el código. Intenta de nuevo." // O un mensaje del backend
            //     _isLoading.value = false
            // }
            // --- FIN LÓGICA DE BACKEND (SIMULADA) ---

            // Sin backend por ahora, solo avanzamos:
            // _currentStep.value = RecoveryStep.EnterCode
            // _isLoading.value = false
        }
    }

    fun submitCode() {
        if (code.value.length < 6) { // Ejemplo de validación simple
            _errorMessage.value = "El código debe tener al menos 6 caracteres."
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            // --- LÓGICA DE BACKEND (SIMULADA) ---
            delay(1500)
            // val result = authRepository.validateRecoveryCode(email.value, code.value)
            // if (result.isSuccess) {
            _currentStep.value = RecoveryStep.EnterNewPassword
            _isLoading.value = false
            _errorMessage.value = null
            // } else {
            //     _errorMessage.value = "Código inválido."
            //     _isLoading.value = false
            // }
            // --- FIN LÓGICA DE BACKEND (SIMULADA) ---
        }
    }

    fun submitNewPassword() {
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
            // --- LÓGICA DE BACKEND (SIMULADA) ---
            delay(2000)
            // val result = authRepository.resetPassword(email.value, code.value, password.value)
            // if (result.isSuccess) {
            _errorMessage.value = "Contraseña cambiada con éxito." // O navegar a Login
            // _currentStep.value = RecoveryStep.Success // O navegar a otra pantalla
            _isLoading.value = false
            // Aquí podrías querer navegar de vuelta al Login o mostrar un mensaje de éxito
            // y luego limpiar el estado o usar un evento para la navegación.
            // } else {
            //     _errorMessage.value = "Error al cambiar la contraseña."
            //     _isLoading.value = false
            // }
            // --- FIN LÓGICA DE BACKEND (SIMULADA) ---
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}