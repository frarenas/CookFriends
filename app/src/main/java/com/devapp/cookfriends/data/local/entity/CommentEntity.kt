package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devapp.cookfriends.domain.model.Comment
import kotlin.uuid.Uuid

@Entity(
    tableName = "comment_table",
    foreignKeys = [ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recipe_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class CommentEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "user_id") val userId: Uuid,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: Uuid
)

fun Comment.toDatabase() = CommentEntity(
    id = id,
    comment = comment,
    userId = userId,
    recipeId = recipeId
)
