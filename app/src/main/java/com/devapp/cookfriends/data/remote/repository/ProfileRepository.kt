package com.devapp.cookfriends.data.remote.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.User
import com.devapp.cookfriends.domain.model.toDomain
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.uuid.Uuid

class ProfileRepository @Inject constructor(
    private val service: CookFriendsService,
    private val dataStore: DataStore<Preferences>
) {

    suspend fun login(username: String, password: String): User? {
        val user = service.login(username, password)
        if (user != null) {
            saveLoggedUserId(user.id)
        }
        return user?.toDomain()
    }

    suspend fun updatePassword(newPassword: String) {
        // TODO: Implementar lógica para actualizar la contraseña en el servidor
    }

    suspend fun saveLoggedUserId(userId: Uuid) {
        dataStore.edit { preferences ->
            preferences[userIdKey] = userId.toString()
        }
    }

    suspend fun  getLoggedUserId(): Uuid? {
        val userId = dataStore.data.map { preferences -> preferences[userIdKey]}.firstOrNull()
        return if (userId != null)
            Uuid.parse(userId)
        else
            null
    }

    private companion object {
        private val userIdKey = stringPreferencesKey("user_id")
    }
}
