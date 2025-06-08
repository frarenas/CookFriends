package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.remote.model.IngredientModel
import kotlin.uuid.Uuid

data class Ingredient(
    var id: Uuid = Uuid.random(),
    var name: String? = null,
    var quantity: String? = null,
    var measurement: String? = null,
    var recipeId: Uuid
)

fun IngredientModel.toDomain() = Ingredient(id, name, quantity, measurement, recipeId)
