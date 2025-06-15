package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import com.devapp.cookfriends.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(): Flow<List<Recipe>>{
        val recipes = repository.getRecipesFromApi()

        if(recipes.isNotEmpty()){
            repository.saveRecipes(recipes)
        }
        // TODO: pasar uuid del usuario logueado
        return repository.getRecipesFromDatabase()
    }
}