package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import com.devapp.cookfriends.data.remote.repository.RecipeTypeRepository
import javax.inject.Inject

class FetchDataUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val recipeTypeRepository: RecipeTypeRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke() {
        val loggedUserId = profileRepository.getLoggedUserId()
        val recipes = recipeRepository.getRecipesFromApi(loggedUserId)
        if(recipes.isNotEmpty()){
            recipeRepository.saveRecipes(recipes)
        }

        val recipeTypes = recipeTypeRepository.getRecipeTypesFromApi()
        if(recipeTypes.isNotEmpty()){
            recipeTypeRepository.saveRecipeTypes(recipeTypes)
        }
    }
}
