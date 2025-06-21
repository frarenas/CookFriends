package com.devapp.cookfriends.data.remote.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.User
import com.devapp.cookfriends.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.uuid.Uuid

class ProfileRepository @Inject constructor(
    private val service: CookFriendsService,
    private val dataStore: DataStore<Preferences>
) {

    suspend fun login(username: String, password: String, keepMeLoggedIn: Boolean): User? {
        val user = service.login(username, password)
        if (user != null) {
            setLoggedUserId(user.id)
            setKeepMeLoggedIn(keepMeLoggedIn)
        }
        return user?.toDomain()
    }

    suspend fun updatePassword(newPassword: String) {
        // TODO: Implementar lógica para actualizar la contraseña en el servidor
    }

    suspend fun setLoggedUserId(userId: Uuid) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId.toString()
        }
    }

    suspend fun  getLoggedUserId(): Uuid? {
        val userId = dataStore.data.map { preferences -> preferences[USER_ID]}.firstOrNull()
        return if (userId != null)
            Uuid.parse(userId)
        else
            null
    }

    suspend fun cleanLoggedUserId() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(KEEP_ME_LOGGED_IN)
        }
    }

    val keepMeLoggedInFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[KEEP_ME_LOGGED_IN] == true
        }

    suspend fun setKeepMeLoggedIn(keepLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEEP_ME_LOGGED_IN] = keepLoggedIn
        }
    }

    private companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val KEEP_ME_LOGGED_IN = booleanPreferencesKey("keep_me_logged_in")

    }
}
