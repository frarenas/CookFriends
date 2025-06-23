package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsSessionActiveUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.keepMeLoggedInFlow
    }
}
