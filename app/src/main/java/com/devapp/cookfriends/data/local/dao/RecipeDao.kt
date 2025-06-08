package com.devapp.cookfriends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.devapp.cookfriends.data.local.entity.IngredientEntity
import com.devapp.cookfriends.data.local.entity.RecipeEntity
import com.devapp.cookfriends.data.local.entity.RecipeWithExtraData
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Transaction
    suspend fun insert(recipe: RecipeWithExtraData) {
        insertRecipe(recipe.recipe)
        if (recipe.ingredients.isNotEmpty()) {
            insertIngredients(recipe.ingredients)
        }
    }

    @Transaction
    suspend fun insert(recipes: List<RecipeWithExtraData>) {
        for (recipe in recipes) {
            insert(recipe)
        }
    }

    @Transaction
    @Query("SELECT * FROM recipe_table")
    fun getRecipes(): Flow<List<RecipeWithExtraData>>

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE id = :id LIMIT 1")
    fun getById(id: Uuid): RecipeWithExtraData?
}
