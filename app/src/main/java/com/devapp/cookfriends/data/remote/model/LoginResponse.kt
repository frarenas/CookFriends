package com.devapp.cookfriends.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("status" ) val status:  String,
    @SerialName("message") val message: String?,
    @SerialName("user"   ) val user:    UserModel?
)
