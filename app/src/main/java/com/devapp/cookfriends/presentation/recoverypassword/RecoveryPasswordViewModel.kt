package com.devapp.cookfriends.presentation.recoverypassword

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.devapp.cookfriends.domain.models.RecoveryStep

class RecoveryPasswordViewModel : ViewModel() {

    var currentStep = mutableStateOf<RecoveryStep>(RecoveryStep.EnterEmail)

    val email = mutableStateOf("")
    val code = mutableStateOf("")
    val password = mutableStateOf("")

    fun nextStep() {
        currentStep.value = when (currentStep.value) {
            is RecoveryStep.EnterEmail -> RecoveryStep.EnterCode
            is RecoveryStep.EnterCode -> RecoveryStep.EnterNewPassword
            is RecoveryStep.EnterNewPassword -> RecoveryStep.EnterNewPassword // o mostrar éxito
        }
    }

    fun goBack() {
        currentStep.value = when (currentStep.value) {
            is RecoveryStep.EnterEmail -> RecoveryStep.EnterEmail
            is RecoveryStep.EnterCode -> RecoveryStep.EnterEmail
            is RecoveryStep.EnterNewPassword -> RecoveryStep.EnterCode
        }
    }

    fun submitNewPassword() {
        // Aquí podrías agregar lógica para guardar o enviar la nueva contraseña
        println("Password submitted: ${password.value}")
        // Resetear a paso inicial o ir a una pantalla de éxito
        currentStep.value = RecoveryStep.EnterEmail
        email.value = ""
        code.value = ""
        password.value = ""
    }
}
