package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import com.devapp.cookfriends.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.uuid.Uuid

class GetRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(id: Uuid): Flow<Recipe?>{
        val loggedUserId = profileRepository.getLoggedUserId()
        return recipeRepository.getRecipeById(
            id,
            loggedUserId
        )
    }
}
