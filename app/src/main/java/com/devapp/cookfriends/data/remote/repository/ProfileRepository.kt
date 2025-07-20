package com.devapp.cookfriends.data.remote.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devapp.cookfriends.data.local.dao.UserDao
import com.devapp.cookfriends.data.remote.model.ChangePasswordRequest
import com.devapp.cookfriends.data.remote.model.Status
import com.devapp.cookfriends.data.remote.model.UserModel
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.User
import com.devapp.cookfriends.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.uuid.Uuid

class ProfileRepository @Inject constructor(
    private val service: CookFriendsService,
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao
) {

    suspend fun login(username: String, password: String, keepMeLoggedIn: Boolean): User {
        val loginResponse = service.login(username, password)
        if (loginResponse.status == Status.SUCCESS.value) {
            setLoggedUserId(loginResponse.user!!.id)
            setKeepMeLoggedIn(keepMeLoggedIn)
            return loginResponse.user.toDomain()
        } else {
            throw Exception(loginResponse.message)
        }
    }

    suspend fun updatePassword(newPassword: String) {
        val user = getLoggedUser()
        val changePasswordRequest = ChangePasswordRequest(user.username, newPassword)
        val changePasswordResponse = service.changePassword(changePasswordRequest)
        if (changePasswordResponse.status == Status.ERROR.value) {
            throw Exception(changePasswordResponse.message)
        }
    }

    suspend fun updatePasswordByUsername(username: String, newPassword: String) {
        val request = ChangePasswordRequest(username = username, newPassword = newPassword)
        val response = service.changePassword(request)
        if (response.status == Status.ERROR.value) {
            throw Exception(response.message)
        }
    }

    suspend fun setLoggedUserId(userId: Uuid) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId.toString()
        }
    }

    suspend fun getLoggedUserId(): Uuid? {
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

    suspend fun getLoggedUser(): User {
        return withContext(Dispatchers.IO) {
            val userId = getLoggedUserId()
            userDao.getById(userId!!).toDomain()
        }
    }

    suspend fun findUserByUsername(username: String): UserModel? {
        return try {
            println("Buscando usuario con username: $username")
            val response = service.getUser(username)

            val rawBody = response.errorBody()?.string() ?: response.body()?.toString() ?: "null"
            println("Raw Body: $rawBody")

            if (response.isSuccessful && rawBody != "null") {
                println("Respuesta exitosa del servicio")
                response.body()
            } else {
                println("Respuesta no exitosa o body nulo")
                null
            }
        } catch (e: Exception) {
            println("Excepción en findUserByUsername: ${e.message}")
            null
        }
    }

    private companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val KEEP_ME_LOGGED_IN = booleanPreferencesKey("keep_me_logged_in")

    }
}
