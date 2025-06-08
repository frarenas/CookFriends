package com.devapp.cookfriends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devapp.cookfriends.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM recipe_table")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipe_table WHERE id = :id LIMIT 1")
    fun getById(id: Int): RecipeEntity?
}
