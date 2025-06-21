package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.UserEntity
import com.devapp.cookfriends.data.remote.model.UserModel
import kotlin.uuid.Uuid

data class User(
    val id: Uuid,
    val username: String,
    val password: String? = null,
    val name: String
)

fun UserModel.toDomain() = User(id, username, password, name)

fun UserEntity.toDomain() = User(id, username, null, name)
