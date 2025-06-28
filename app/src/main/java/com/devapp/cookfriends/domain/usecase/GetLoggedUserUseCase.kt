package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import com.devapp.cookfriends.domain.model.User
import javax.inject.Inject

class GetLoggedUserUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend operator fun invoke(): User {
        return repository.getLoggedUser()
    }
}
