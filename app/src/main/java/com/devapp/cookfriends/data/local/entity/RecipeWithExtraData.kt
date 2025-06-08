package com.devapp.cookfriends.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.devapp.cookfriends.domain.model.Recipe

data class RecipeWithExtraData(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val ingredients: List<IngredientEntity>
)

fun Recipe.toDatabase() = RecipeWithExtraData(
    recipe = RecipeEntity( id = id,
    name = name,
    author = author,
    rate = rate,
    favorite = favorite,
    type = type,
    portions = portions),
    ingredients = ingredients.map { ingredient -> ingredient.toDatabase() }
)
