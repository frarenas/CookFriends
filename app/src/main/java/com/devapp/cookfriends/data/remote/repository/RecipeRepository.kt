package com.devapp.cookfriends.data.remote.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.devapp.cookfriends.data.local.dao.RecipeDao
import com.devapp.cookfriends.data.local.entity.RecipeWithExtraData
import com.devapp.cookfriends.data.local.entity.toDatabase
import com.devapp.cookfriends.data.remote.model.ApiResponse
import com.devapp.cookfriends.data.remote.model.RecipeModel
import com.devapp.cookfriends.data.remote.model.toModel
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.uuid.Uuid

class RecipeRepository @Inject constructor(
    private val service: CookFriendsService,
    private val recipeDao: RecipeDao
) {

    suspend fun getRecipesFromApi(userId: Uuid?): List<Recipe> {
        val recipes: List<RecipeModel> = service.getRecipes(userId)
        return recipes.map { it.toDomain() }
    }

    suspend fun upsertRecipesToApi(recipe: Recipe): ApiResponse =
        service.upsertRecipe(recipe.toModel())

    fun getRecipesFromDatabase(query: SupportSQLiteQuery): Flow<List<Recipe>> {
        val recipesEntityFlow: Flow<List<RecipeWithExtraData>> = recipeDao.getDynamicRecipes(query)
        return recipesEntityFlow.map { listOfEntities ->
            listOfEntities.map { entity ->
                entity.toDomain()
            }
        }
    }

    suspend fun saveRecipes(recipes: List<Recipe>) {
        withContext(Dispatchers.IO) {
            val recipesEntities: List<RecipeWithExtraData> =
                recipes.map { recipe -> recipe.toDatabase() }
            recipeDao.insert(recipesEntities)
        }
    }

    suspend fun saveRecipe(recipe: Recipe) =
        withContext(Dispatchers.IO) {
            recipeDao.insert(recipe.toDatabase())
        }

    suspend fun getRecipeById(id: Uuid, userId: Uuid? = null): Flow<Recipe?> {
        return withContext(Dispatchers.IO) {
            recipeDao.getById(id, userId).map { it?.toDomain() }
        }
    }

    suspend fun setUpdated(id: Uuid) {
        return withContext(Dispatchers.IO) {
            recipeDao.setUpdated(id)
        }
    }

    suspend fun getPendingUploadRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            recipeDao.getPendingUploadRecipes().map { it.toDomain() }
        }
    }

    suspend fun deleteRecipe(id: Uuid) =
        withContext(Dispatchers.IO) {
            recipeDao.deleteRecipeById(id)
        }
}
