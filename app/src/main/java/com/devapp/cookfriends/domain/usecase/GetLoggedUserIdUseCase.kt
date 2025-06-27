package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import javax.inject.Inject
import kotlin.uuid.Uuid

class GetLoggedUserIdUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend operator fun invoke(): Uuid? {
        return repository.getLoggedUserId()
    }
}
