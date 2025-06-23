package com.devapp.cookfriends.data.local.database

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

class UuidConverter {

    @TypeConverter
    fun fromUUID(uuid: Uuid): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String): Uuid {
        return Uuid.parse(string)
    }
}
