package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import javax.inject.Inject

class IsUserLoggedUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.getLoggedUserId() != null
    }
}
