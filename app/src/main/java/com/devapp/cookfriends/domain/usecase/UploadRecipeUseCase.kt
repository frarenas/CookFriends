package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.model.Status
import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import com.devapp.cookfriends.domain.model.Recipe
import javax.inject.Inject

class UploadRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipe: Recipe) {
        val apiResponse = repository.upsertRecipesToApi(recipe)
        if (apiResponse.status == Status.SUCCESS.value) {
            repository.setUpdated(recipe.id)
        }
    }
}
