package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.CommentEntity
import com.devapp.cookfriends.data.remote.model.CommentModel
import kotlin.uuid.Uuid

data class Comment(
    var id: Uuid = Uuid.random(),
    var comment: String,
    var userId: Uuid,
    var recipeId: Uuid
)

fun CommentModel.toDomain() = Comment(id, comment, userId, recipeId)

fun CommentEntity.toDomain() = Comment(id, comment, userId, recipeId)
