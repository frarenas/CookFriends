package com.devapp.cookfriends.data.remote.repository

import com.devapp.cookfriends.data.local.dao.RatingDao
import com.devapp.cookfriends.data.local.entity.toDatabase
import com.devapp.cookfriends.data.remote.model.toModel
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.Rating
import com.devapp.cookfriends.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.uuid.Uuid

class RatingRepository @Inject constructor(
    private val service: CookFriendsService,
    private val ratingDao: RatingDao
) {

    suspend fun saveRating(rating: Rating) =
        withContext(Dispatchers.IO) {
            ratingDao.insert(rating.toDatabase())
        }

    suspend fun rateRecipe(rating: Rating) =
        withContext(Dispatchers.IO) {
            service.rateRecipe(rating.toModel())
        }

    suspend fun getRating(userId: Uuid, recipeId: Uuid): Rating? =
        withContext(Dispatchers.IO) {
            return@withContext ratingDao.getRatingByUserIdAndRecipeId(userId, recipeId)?.toDomain()
        }
}
