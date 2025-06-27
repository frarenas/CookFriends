package com.devapp.cookfriends.data.remote.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncStatusRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun setFirsSync(synced: Boolean) {
        dataStore.edit { preferences ->
            preferences[SYNCED] = synced
        }
    }

    suspend fun isFirsSynced(): Boolean {
        val synced = dataStore.data.map { preferences -> preferences[SYNCED]}.firstOrNull()
        return synced == true
    }

    private companion object {
        val SYNCED = booleanPreferencesKey("synced")
    }
}
