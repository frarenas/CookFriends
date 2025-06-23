package com.devapp.cookfriends.presentation.navigation

import android.os.Bundle
import androidx.navigation.NavType
import kotlin.uuid.Uuid

val UuidNavType = object : NavType<Uuid?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Uuid? {
        return bundle.getString(key)?.let { Uuid.parse(it) }
    }

    override fun parseValue(value: String): Uuid? {
        return try {
            Uuid.parse(value)
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    override fun put(bundle: Bundle, key: String, value: Uuid?) {
        bundle.putString(key, value?.toString())
    }

    override val name: String
        get() = "uuid"
}
