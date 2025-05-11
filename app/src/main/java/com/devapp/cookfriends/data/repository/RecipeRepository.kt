package com.devapp.cookfriends.data.repository

import com.devapp.cookfriends.domain.models.Recipe
import kotlinx.coroutines.delay
import javax.inject.Inject

class RecipeRepository @Inject constructor() {

    suspend fun getRecipes(): List<Recipe> {
        delay(3000)
        return listOf(
            Recipe(name = "Pizza", author = "Juan Perez", rate = 3.7, favorite = true, type = "Italian"),
            Recipe(name = "Pizza", author = "Juan Perez", rate = 3.7, favorite = true, type = "Italian"),
            Recipe(name = "Pizza", author = "Juan Perez", rate = 3.7, favorite = true, type = "Italian"),
            Recipe(name = "Pizza", author = "Juan Perez", rate = 3.7, favorite = true, type = "Italian"),
            Recipe(name = "Pizza", author = "Juan Perez", rate = 3.7, favorite = true, type = "Italian")
        )
    }
}