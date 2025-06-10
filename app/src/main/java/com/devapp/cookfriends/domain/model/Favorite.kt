package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.FavoriteEntity
import com.devapp.cookfriends.data.remote.model.FavoriteModel
import kotlin.uuid.Uuid

data class Favorite(
    var id: Uuid = Uuid.random(),
    var userId: Uuid,
    var recipeId: Uuid
)

fun FavoriteModel.toDomain() = Favorite(id, userId, recipeId)

fun FavoriteEntity.toDomain() = Favorite(id, userId, recipeId)
