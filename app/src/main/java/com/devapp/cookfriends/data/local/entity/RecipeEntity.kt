package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(tableName = "recipe_table")
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "author") val author: String? = null,
    @ColumnInfo(name = "rate") val rate: Double? = null,
    @ColumnInfo(name = "favorite") val favorite: Boolean? = null,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "portions") val portions: Int? = null
)
