package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.model.Status
import com.devapp.cookfriends.data.remote.repository.RatingRepository
import com.devapp.cookfriends.domain.model.Rating
import javax.inject.Inject

class RateRecipeUseCase @Inject constructor(private val repository: RatingRepository) {
    suspend operator fun invoke(rating: Rating) {
        val apiResponse = repository.rateRecipe(rating)
        if (apiResponse.status == Status.SUCCESS.value) {
            val currentRating = repository.getRating(rating.userId, rating.recipeId)
            if (currentRating != null) {
                rating.id = currentRating.id
            }
            repository.saveRating(rating)
        } else {
            throw Exception(apiResponse.message)
        }
    }
}
