package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.uuid.Uuid

@Entity(tableName = "recipe_table")
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "author") val author: String? = null,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "portions") val portions: Int? = null,
    @ColumnInfo(name = "date") val date: Instant,
)
