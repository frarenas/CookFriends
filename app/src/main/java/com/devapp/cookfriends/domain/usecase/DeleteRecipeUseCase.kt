package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import javax.inject.Inject
import kotlin.uuid.Uuid

class DeleteRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(id: Uuid) {
        repository.deleteRecipe(id)
    }
}
