package com.devapp.cookfriends.data.repository

import javax.inject.Inject

class ProfileRepository @Inject constructor() {

    suspend fun updatePassword(newPassword: String) {
        // TODO: Implementar lógica para actualizar la contraseña en el servidor
    }
}
