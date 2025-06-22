package com.devapp.cookfriends.data.remote.repository

import com.devapp.cookfriends.data.local.dao.FavoriteDao
import com.devapp.cookfriends.data.local.entity.toDatabase
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.Favorite
import com.devapp.cookfriends.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.uuid.Uuid

class FavoriteRepository @Inject constructor(
    private val service: CookFriendsService,
    private val favoriteDao: FavoriteDao
) {

    suspend fun getFavorite(userId: Uuid, recipeId: Uuid): Favorite? =
        withContext(Dispatchers.IO) {
            return@withContext favoriteDao.getFavoritesByUserIdAndRecipeId(userId, recipeId)?.toDomain()
        }

    suspend fun addFavorite(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            favoriteDao.insert(favorite.toDatabase())
        }

    suspend fun removeFavorite(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            favoriteDao.delete(favorite.toDatabase())
        }
}
