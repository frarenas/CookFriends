package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "recipe_table",
    indices = [androidx.room.Index(value = ["recipe_type_id"])]
)
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "user_id") val userId: Uuid?,
    @ColumnInfo(name = "recipe_type_id") val recipeTypeId: Uuid?,
    @ColumnInfo(name = "portions") val portions: Int,
    @ColumnInfo(name = "date") val date: Instant,
)
