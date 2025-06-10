package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devapp.cookfriends.domain.model.Rating
import kotlin.uuid.Uuid

@Entity(
    tableName = "rating_table",
    foreignKeys = [ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recipe_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class RatingEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "rate") val rate: Int,
    @ColumnInfo(name = "user_id") val userId: Uuid,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: Uuid
)

fun Rating.toDatabase() = RatingEntity(
    id = id,
    rate = rate,
    userId = userId,
    recipeId = recipeId
)
