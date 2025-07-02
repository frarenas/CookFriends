package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.CommentWithUser
import com.devapp.cookfriends.data.remote.model.CommentModel
import kotlinx.datetime.Instant
import kotlin.uuid.Uuid

data class Comment(
    var id: Uuid = Uuid.random(),
    var comment: String,
    var recipeId: Uuid,
    var user: User,
    var date: Instant
)

fun CommentModel.toDomain() = Comment(id, comment, recipeId, user.toDomain(), date)

fun CommentWithUser.toDomain() = Comment(
    id = comment.id,
    comment = comment.comment,
    recipeId = comment.recipeId,
    user.toDomain(),
    date = comment.date
)
