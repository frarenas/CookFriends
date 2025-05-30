package com.devapp.cookfriends.domain.models

import com.devapp.cookfriends.data.model.IngredientModel

data class Ingredient(
    var name: String? = null,
    var quantity: String? = null,
    var measurement: String? = null
)

fun IngredientModel.toDomain() = Ingredient(name, quantity, measurement)
