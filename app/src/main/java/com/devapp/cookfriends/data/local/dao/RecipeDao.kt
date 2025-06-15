package com.devapp.cookfriends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.devapp.cookfriends.data.local.entity.CommentEntity
import com.devapp.cookfriends.data.local.entity.FavoriteEntity
import com.devapp.cookfriends.data.local.entity.IngredientEntity
import com.devapp.cookfriends.data.local.entity.PhotoEntity
import com.devapp.cookfriends.data.local.entity.RatingEntity
import com.devapp.cookfriends.data.local.entity.RecipeEntity
import com.devapp.cookfriends.data.local.entity.RecipeWithExtraData
import com.devapp.cookfriends.data.local.entity.StepEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<StepEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRatings(ratings: List<RatingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: List<FavoriteEntity>)

    @Transaction
    suspend fun insert(recipe: RecipeWithExtraData) {
        insertRecipe(recipe.recipe)
        if (recipe.ingredients.isNotEmpty()) {
            insertIngredients(recipe.ingredients)
        }
        if (recipe.steps.isNotEmpty()) {
            insertSteps(recipe.steps)
        }
        if (recipe.photos.isNotEmpty()) {
            insertPhotos(recipe.photos)
        }
        if (recipe.ratings.isNotEmpty()) {
            insertRatings(recipe.ratings)
        }
        if (recipe.comments.isNotEmpty()) {
            insertComments(recipe.comments)
        }
        if (recipe.favorites.isNotEmpty()) {
            insertFavorites(recipe.favorites)
        }
    }

    @Transaction
    suspend fun insert(recipes: List<RecipeWithExtraData>) {
        for (recipe in recipes) {
            insert(recipe)
        }
    }

    @Transaction
    @Query("""
            SELECT 
                r.*,
                (SELECT ROUND(AVG(rt.rate), 2) 
                 FROM rating_table rt 
                 WHERE rt.recipe_id = r.id) AS averageRating,
                (EXISTS (SELECT 1 
                         FROM favorite_table ft 
                         WHERE ft.recipe_id = r.id AND ft.user_id = :userId)) AS isUserFavorite
            FROM recipe_table r
        """)
    fun getRecipes(userId: Uuid? = null): Flow<List<RecipeWithExtraData>>

    @Transaction
    @Query("SELECT *, 0 as isUserFavorite FROM recipe_table WHERE id = :id LIMIT 1")
    fun getById(id: Uuid): RecipeWithExtraData?

    @RawQuery(observedEntities = [RecipeEntity::class])
    fun getDynamicRecipes(query: SupportSQLiteQuery): Flow<List<RecipeWithExtraData>>
}
