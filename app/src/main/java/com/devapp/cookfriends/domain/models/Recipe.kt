package com.devapp.cookfriends.domain.models

import com.devapp.cookfriends.data.model.RecipeModel

data class Recipe(
    var name: String? = null,
    var author: String? = null,
    var rate: Double? = null,
    var favorite: Boolean? = null,
    var type: String? = null,
    var portions: Int? = null,
    var ingredients: List<Ingredient> = arrayListOf(),
    var steps: List<Step> = arrayListOf()
)

fun RecipeModel.toDomain() = Recipe(
    name,
    author,
    null,
    null,
    type,
    portions,
    ingredients.map { ingredient -> ingredient.toDomain() },
    steps.map { step -> step.toDomain() }
)
