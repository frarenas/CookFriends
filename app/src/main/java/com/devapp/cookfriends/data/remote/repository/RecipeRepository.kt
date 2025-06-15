package com.devapp.cookfriends.data.remote.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.devapp.cookfriends.data.local.dao.RecipeDao
import com.devapp.cookfriends.data.local.entity.RecipeWithExtraData
import com.devapp.cookfriends.data.local.entity.toDatabase
import com.devapp.cookfriends.data.remote.model.RecipeModel
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val service: CookFriendsService,
    private val recipeDao: RecipeDao
) {

    suspend fun getRecipesFromApi(): List<Recipe> {
        val recipes: List<RecipeModel> = service.getRecipes()
        return recipes.map { it.toDomain() }
    }

    /*fun getRecipesFromDatabase(userId: Uuid? = null): Flow<List<Recipe>> {
        val recipesEntityFlow: Flow<List<RecipeWithExtraData>> = recipeDao.getRecipes(userId)
        return recipesEntityFlow.map { listOfEntities ->
            listOfEntities.map { entity ->
                entity.toDomain()
            }
        }
    }*/

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
}
