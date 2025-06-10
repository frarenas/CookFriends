package com.devapp.cookfriends.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class CommentModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("comment" ) var comment   : String,
    @SerialName("userId"  ) var userId    : Uuid,
    @SerialName("recipeId") var recipeId  : Uuid
)
