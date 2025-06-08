package com.devapp.cookfriends.data.remote.repository

import com.devapp.cookfriends.data.local.dao.RecipeDao
import com.devapp.cookfriends.data.local.entity.RecipeEntity
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

    /*suspend fun getRecipesFromDatabase(): List<RecipeModel> {
        val recipes: Flow<List<RecipeEntity>> = recipeDao.getRecipes()
        return recipes.map { it.map { recipe -> recipe.toDomain() } }
    }*/

    fun getRecipesFromDatabase(): Flow<List<Recipe>> {
        val recipesEntityFlow: Flow<List<RecipeEntity>> = recipeDao.getRecipes()
        return recipesEntityFlow.map { listOfEntities ->
            listOfEntities.map { entity ->
                entity.toDomain()
            }
        }
    }

    suspend fun saveRecipes(recipes: List<Recipe>) {
        withContext(Dispatchers.IO) {
            val recipesEntities: List<RecipeEntity> = recipes.map { recipe -> recipe.toDatabase() }
            recipeDao.insert(recipesEntities)
        }
    }
}
