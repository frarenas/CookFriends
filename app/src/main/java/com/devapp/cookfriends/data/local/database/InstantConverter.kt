package com.devapp.cookfriends.data.local.database

import androidx.room.TypeConverter
import kotlin.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromInstant(value: Instant?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toInstant(value: String?): Instant? {
        return value?.let {
            try {
                Instant.parse(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
