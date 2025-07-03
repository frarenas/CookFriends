package com.devapp.cookfriends.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.devapp.cookfriends.data.local.entity.UserEntity
import kotlin.uuid.Uuid

@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM user_table WHERE id = :id LIMIT 1")
    fun getById(id: Uuid): UserEntity
}
