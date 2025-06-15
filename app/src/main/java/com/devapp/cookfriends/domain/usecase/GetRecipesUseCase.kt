package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.SearchOptions
import com.devapp.cookfriends.util.SqlQueryBuilder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(searchOptions: SearchOptions): Flow<List<Recipe>>{
        // TODO: pasar uuid del usuario logueado
        return repository.getRecipesFromDatabase(SqlQueryBuilder.getProductsDynamically(null, searchOptions))
    }
}
