package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import com.devapp.cookfriends.domain.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String, password: String, keepMeLoggedIn: Boolean): User {
        if (username.isBlank()) {
            throw Exception("Ingrese el usuario.")
        }
        if (password.isBlank()) {
            throw Exception("Ingrese la contraseña.")
        }
        val user = repository.login(username, password, keepMeLoggedIn)
        if (user == null) {
            throw Exception("Credenciales inválidas.")
        }
        return user
    }
}
