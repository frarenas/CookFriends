package com.devapp.cookfriends.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devapp.cookfriends.data.local.entity.FavoriteEntity
import kotlin.uuid.Uuid

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorite_table WHERE user_id = :userId AND recipe_id = :recipeId LIMIT 1")
    fun getFavoritesByUserIdAndRecipeId(userId: Uuid, recipeId: Uuid): FavoriteEntity?

}
