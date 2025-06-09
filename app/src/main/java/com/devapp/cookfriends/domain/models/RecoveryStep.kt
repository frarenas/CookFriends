package com.devapp.cookfriends.domain.models

sealed class RecoveryStep {
    object EnterEmail : RecoveryStep()
    object EnterCode : RecoveryStep()
    object EnterNewPassword : RecoveryStep()
    //data class Completed(val successMessage: String) : RecoveryStep()
    //data object Error(val errorMessage: String) : RecoveryStep()
    //data object Loading : RecoveryStep()
}