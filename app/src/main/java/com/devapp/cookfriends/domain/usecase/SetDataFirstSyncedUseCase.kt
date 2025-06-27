package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.SyncStatusRepository
import javax.inject.Inject

class SetDataFirstSyncedUseCase @Inject constructor(
    val repository: SyncStatusRepository
) {
    suspend operator fun invoke() {
        return repository.setFirsSync(true)
    }
}
