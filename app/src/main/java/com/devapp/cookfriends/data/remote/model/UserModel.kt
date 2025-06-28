package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class UserModel(
    @SerialName("id"      ) var id       : Uuid,
    @SerialName("username") var username : String,
    @SerialName("password") var password : String? = "",
    @SerialName("name"    ) var name     : String? = ""
)

fun User.toModel() = UserModel(id = id, username = username)
