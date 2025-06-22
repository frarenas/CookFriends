package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend operator fun invoke() {
        repository.cleanLoggedUserId()
        repository.setKeepMeLoggedIn(false)
    }
}
