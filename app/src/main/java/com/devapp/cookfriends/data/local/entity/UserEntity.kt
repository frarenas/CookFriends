package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devapp.cookfriends.domain.model.User
import kotlin.uuid.Uuid

@Entity(
    tableName = "user_table"
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "username") val username: String
)

fun User.toDatabase() = UserEntity(
    id = id,
    username = username
)
