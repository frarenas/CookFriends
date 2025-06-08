package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devapp.cookfriends.domain.models.Recipe

@Entity(tableName = "recipe_table")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "author") val author: String? = null,
    @ColumnInfo(name = "rate") val rate: Double? = null,
    @ColumnInfo(name = "favorite") val favorite: Boolean? = null,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "portions") val portions: Int? = null,
    //@ColumnInfo(name = "ingredients") val ingredients: List<Ingredient> = arrayListOf(),
    //@ColumnInfo(name = "steps") val steps: List<Step> = arrayListOf()
)

fun Recipe.toDatabase() = Recipe(
    name = name,
    author = author,
    rate = rate,
    favorite = favorite,
    type = type,
    portions = portions,
    //ingredients = ingredients,
    //steps = steps
)
