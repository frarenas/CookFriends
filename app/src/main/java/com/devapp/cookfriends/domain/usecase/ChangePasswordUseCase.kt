package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.repository.ProfileRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend fun execute(newPassword: String, repeatPassword: String) {
        if (newPassword.isBlank()) {
            throw Exception("La contraseña no puede estar vacía.")
        }
        if (newPassword != repeatPassword) {
            throw Exception("Las contraseñas no coinciden.")
        }
        repository.updatePassword(newPassword)
    }
}
