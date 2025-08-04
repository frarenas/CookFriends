package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.Comment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class CommentModel(
    @SerialName("id"       ) var id        : Uuid,
    @SerialName("comment"  ) var comment   : String,
    @SerialName("recipeId" ) var recipeId  : Uuid,
    @SerialName("user"     ) var user      : UserModel,
    @SerialName("date"     ) var date      : Instant,
    @SerialName("published") var published : Boolean
)

fun Comment.toModel() = CommentModel(
    id = id,
    comment = comment,
    recipeId = recipeId,
    user = user.toModel(),
    date = date,
    published = published
)
