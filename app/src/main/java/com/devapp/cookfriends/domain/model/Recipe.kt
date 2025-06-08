package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.RecipeEntity
import com.devapp.cookfriends.data.remote.model.RecipeModel
import kotlin.uuid.Uuid

data class Recipe(
    var id: Uuid = Uuid.random(),
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
    id,
    name,
    author,
    null,
    null,
    type,
    portions,
    ingredients.map { ingredient -> ingredient.toDomain() },
    steps.map { step -> step.toDomain() }
)

fun RecipeEntity.toDomain() = Recipe(
    id,
    name,
    author,
    null,
    null,
    type,
    portions,
    //ingredients.map { ingredient -> ingredient.toDomain() },
    //steps.map { step -> step.toDomain() }
)
