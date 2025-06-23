package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.RatingEntity
import com.devapp.cookfriends.data.remote.model.RatingModel
import kotlin.uuid.Uuid

data class Rating(
    var id: Uuid = Uuid.random(),
    var rate: Int,
    var userId: Uuid,
    var recipeId: Uuid
)

fun RatingModel.toDomain() = Rating(id, rate, userId, recipeId)

fun RatingEntity.toDomain() = Rating(id, rate, userId, recipeId)
