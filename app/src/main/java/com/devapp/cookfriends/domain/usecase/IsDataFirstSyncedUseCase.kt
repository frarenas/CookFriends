package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.SyncStatusRepository
import javax.inject.Inject

class IsDataFirstSyncedUseCase @Inject constructor(
    val repository: SyncStatusRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isFirsSynced()
    }
}
