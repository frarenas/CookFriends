package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import javax.inject.Inject

class FetchDataUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(){
        val recipes = repository.getRecipesFromApi()

        if(recipes.isNotEmpty()){
            repository.saveRecipes(recipes)
        }
    }
}
