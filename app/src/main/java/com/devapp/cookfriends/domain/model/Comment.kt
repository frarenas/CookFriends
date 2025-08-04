package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.CommentWithUser
import com.devapp.cookfriends.data.remote.model.CommentModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Comment(
    var id: Uuid = Uuid.random(),
    var comment: String,
    var recipeId: Uuid,
    var user: User,
    var date: Instant,
    var updatePending: Boolean = false,
    var published: Boolean = false
)

fun CommentModel.toDomain() = Comment(
    id = id,
    comment = comment,
    recipeId = recipeId,
    user = user.toDomain(),
    date = date,
    published = published
)

fun CommentWithUser.toDomain() = Comment(
    id = comment.id,
    comment = comment.comment,
    recipeId = comment.recipeId,
    user.toDomain(),
    date = comment.date,
    published = comment.published
)
