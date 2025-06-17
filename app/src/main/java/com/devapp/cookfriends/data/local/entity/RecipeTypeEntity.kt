package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devapp.cookfriends.domain.model.RecipeType
import kotlin.uuid.Uuid

@Entity(
    tableName = "recipe_type_table"
)
data class RecipeTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "name") val name: String
)

fun RecipeType.toDatabase() = RecipeTypeEntity(
    id = id,
    name = name
)
