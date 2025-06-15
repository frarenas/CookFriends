package com.devapp.cookfriends.data.remote.repository

import com.devapp.cookfriends.data.local.dao.RecipeTypeDao
import com.devapp.cookfriends.data.local.entity.RecipeTypeEntity
import com.devapp.cookfriends.data.local.entity.toDatabase
import com.devapp.cookfriends.data.remote.model.RecipeTypeModel
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.RecipeType
import com.devapp.cookfriends.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeTypeRepository @Inject constructor(
    private val service: CookFriendsService,
    private val recipeTypeDao: RecipeTypeDao
) {

    suspend fun getRecipeTypesFromApi(): List<RecipeType> {
        val recipeTypes: List<RecipeTypeModel> = service.getRecipeTypes()
        return recipeTypes.map { it.toDomain() }
    }

    fun getRecipeTypesFromDatabase(): Flow<List<RecipeType>> {
        val recipeTypesEntityFlow: Flow<List<RecipeTypeEntity>> = recipeTypeDao.getRecipeTypes()
        return recipeTypesEntityFlow.map { listOfEntities ->
            listOfEntities.map { entity ->
                entity.toDomain()
            }
        }
    }

    suspend fun saveRecipeTypes(recipeTypes: List<RecipeType>) {
        withContext(Dispatchers.IO) {
            val recipeTypesEntities: List<RecipeTypeEntity> =
                recipeTypes.map { recipeType -> recipeType.toDatabase() }
            recipeTypeDao.insert(recipeTypesEntities)
        }
    }
}
