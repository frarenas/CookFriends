package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.RecipeTypeRepository
import com.devapp.cookfriends.domain.model.RecipeType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeTypesUseCase @Inject constructor(private val repository: RecipeTypeRepository) {
    operator fun invoke(): Flow<List<RecipeType>>{
        return repository.getRecipeTypesFromDatabase()
    }
}
