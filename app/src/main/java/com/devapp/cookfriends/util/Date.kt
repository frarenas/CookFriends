package com.devapp.cookfriends.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun Instant.toShortFormat() : String {
    val timeZone = TimeZone.currentSystemDefault()

    val localDateTime = this.toLocalDateTime(timeZone)
    return  localDateTime.format(LocalDateTime.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    })
}