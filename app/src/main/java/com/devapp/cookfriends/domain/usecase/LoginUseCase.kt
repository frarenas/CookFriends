package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import com.devapp.cookfriends.domain.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String, password: String, keepMeLoggedIn: Boolean): User {
        return repository.login(username, password, keepMeLoggedIn)
    }
}
