package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.Favorite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class FavoriteModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("userId"  ) var userId    : Uuid,
    @SerialName("recipeId") var recipeId  : Uuid
)

fun Favorite.toModel() = FavoriteModel(id, userId, recipeId)
