package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import javax.inject.Inject
import kotlin.uuid.Uuid

class CountUserCalculatedRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(userId: Uuid): Int {
        return recipeRepository.countUserCalculatedRecipes(userId)
    }
}
