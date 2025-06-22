package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.repository.FavoriteRepository
import com.devapp.cookfriends.data.remote.repository.ProfileRepository
import com.devapp.cookfriends.domain.model.Favorite
import javax.inject.Inject
import kotlin.uuid.Uuid

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(recipeId: Uuid) {
        val loggedUserId = profileRepository.getLoggedUserId()
        if (loggedUserId != null) {
            var favorite = favoriteRepository.getFavorite(loggedUserId, recipeId)
            if (favorite == null) {
                favorite = Favorite(
                    id = Uuid.random(),
                    userId = loggedUserId,
                    recipeId = recipeId
                )
                favoriteRepository.addFavorite(favorite)
            } else {
                favoriteRepository.removeFavorite(favorite)
            }
        }
    }
}
