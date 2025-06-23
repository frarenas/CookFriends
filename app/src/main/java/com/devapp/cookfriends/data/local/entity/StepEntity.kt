package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "step_table",
    foreignKeys = [ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recipe_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class StepEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "order") val order: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: Uuid
)
