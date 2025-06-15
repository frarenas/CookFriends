package com.devapp.cookfriends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devapp.cookfriends.data.local.entity.RecipeTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipeType: List<RecipeTypeEntity>)

    @Query("SELECT * FROM recipe_type_table")
    fun getRecipeTypes(): Flow<List<RecipeTypeEntity>>
}
