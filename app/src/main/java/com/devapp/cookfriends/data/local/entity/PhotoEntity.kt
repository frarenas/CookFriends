package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devapp.cookfriends.domain.model.Photo
import kotlin.uuid.Uuid

@Entity(
    tableName = "photo_table",
    foreignKeys = [ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recipe_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: Uuid
)

fun Photo.toDatabase() = PhotoEntity(
    id = id,
    url = url,
    recipeId = recipeId
)
