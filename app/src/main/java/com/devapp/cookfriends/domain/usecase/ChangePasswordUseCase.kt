package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend operator fun invoke(newPassword: String) {
        repository.updatePassword(newPassword)
    }
}
