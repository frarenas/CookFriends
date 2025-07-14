package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.model.UserModel
import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import jakarta.inject.Inject

class GetUsernameUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String): UserModel? {
        return repository.findUserByUsername(username)
    }
}