package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import jakarta.inject.Inject

class ChangePasswordByUsernameUseCase @Inject constructor(
    val repository: ProfileRepository
){
    suspend operator fun invoke(username: String, newPassword: String) {
        repository.updatePasswordByUsername(username, newPassword)
    }

}