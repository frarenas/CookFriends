package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.remote.model.IngredientModel

data class Ingredient(
    var name: String? = null,
    var quantity: String? = null,
    var measurement: String? = null
)

fun IngredientModel.toDomain() = Ingredient(name, quantity, measurement)
