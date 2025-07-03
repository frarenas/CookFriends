package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.Rating
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class RatingModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("rate"    ) var rate      : Int,
    @SerialName("userId"  ) var userId    : Uuid,
    @SerialName("recipeId") var recipeId  : Uuid
)

fun Rating.toModel() = RatingModel(
    id = id,
    rate = rate,
    userId = userId,
    recipeId = recipeId
)
