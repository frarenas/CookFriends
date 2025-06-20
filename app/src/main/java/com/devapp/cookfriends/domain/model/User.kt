package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.UserEntity
import com.devapp.cookfriends.data.remote.model.UserModel
import kotlin.uuid.Uuid

data class User(
    val id: Uuid,
    val username: String
)

fun UserModel.toDomain() = User(id, username)

fun UserEntity.toDomain() = User(id, username)
