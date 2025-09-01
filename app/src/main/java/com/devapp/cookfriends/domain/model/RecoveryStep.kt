package com.devapp.cookfriends.domain.model

sealed class RecoveryStep {
    object EnterEmail : RecoveryStep()
    object EnterCode : RecoveryStep()
    object EnterNewPassword : RecoveryStep()
}
