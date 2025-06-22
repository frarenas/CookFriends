package com.devapp.cookfriends.data.remote.repository

import com.devapp.cookfriends.data.local.dao.FavoriteDao
import com.devapp.cookfriends.data.local.entity.toDatabase
import com.devapp.cookfriends.data.remote.model.toModel
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

    suspend fun addFavoriteDb(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            favoriteDao.insert(favorite.toDatabase())
        }

    suspend fun removeFavoriteDb(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            favoriteDao.delete(favorite.toDatabase())
        }

    suspend fun addFavoriteApi(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            service.addFavorite(favorite.toModel())
        }

    suspend fun removeFavoriteApi(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            service.removeFavorite(favorite.toModel())
        }
}
