package com.devapp.cookfriends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devapp.cookfriends.data.local.entity.RatingEntity
import kotlin.uuid.Uuid

@Dao
interface RatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rating: RatingEntity)

    @Query("SELECT * FROM rating_table WHERE user_id = :userId AND recipe_id = :recipeId LIMIT 1")
    fun getRatingByUserIdAndRecipeId(userId: Uuid, recipeId: Uuid): RatingEntity?
}
