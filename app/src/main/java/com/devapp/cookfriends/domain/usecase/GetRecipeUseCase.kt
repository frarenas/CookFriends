package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import com.devapp.cookfriends.domain.model.Recipe
import javax.inject.Inject
import kotlin.uuid.Uuid

class GetRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {
    operator fun invoke(id: Uuid): Recipe?{
        return repository.getRecipeById(id)
    }
}
