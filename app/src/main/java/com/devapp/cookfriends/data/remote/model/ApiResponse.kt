package com.devapp.cookfriends.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    @SerialName("status" ) val status:  String,
    @SerialName("message") val message: String
)

enum class Status(val value: String) {
    SUCCESS("success"),
    ERROR("error")
}