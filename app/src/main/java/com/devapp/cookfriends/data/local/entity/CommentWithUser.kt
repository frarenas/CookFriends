package com.devapp.cookfriends.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.devapp.cookfriends.domain.model.Comment

data class CommentWithUser(
    @Embedded
    val comment: CommentEntity,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "id",
        entity = UserEntity::class
    )
    val user: UserEntity
)

fun Comment.toDatabase() = CommentWithUser(
    comment = CommentEntity(
        id = id,
        comment = comment,
        recipeId = recipeId,
        date = date,
        userId = user.id,
        updatePending = updatePending,
        published = published
    ),
    user = user.toDatabase()
)
