package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.IngredientEntity
import com.devapp.cookfriends.data.remote.model.IngredientModel
import kotlin.uuid.Uuid

data class Ingredient(
    var id: Uuid = Uuid.random(),
    var name: String,
    var quantity: String,
    var measurement: String? = null,
    var recipeId: Uuid
)

fun IngredientModel.toDomain() = Ingredient(id, name, quantity, measurement, recipeId)

fun IngredientEntity.toDomain() = Ingredient(id, name, quantity, measurement, recipeId)
