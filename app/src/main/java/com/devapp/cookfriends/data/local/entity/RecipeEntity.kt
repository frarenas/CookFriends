package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "recipe_table",
    /*foreignKeys = [
        ForeignKey(
            entity = RecipeTypeEntity::class,
            parentColumns = ["recipe_type_id"],
            childColumns = ["id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],*/
    indices = [androidx.room.Index(value = ["recipe_type_id"])]
)
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "recipe_type_id") val recipeTypeId: Uuid?,
    @ColumnInfo(name = "portions") val portions: Int,
    @ColumnInfo(name = "date") val date: Instant,
)
