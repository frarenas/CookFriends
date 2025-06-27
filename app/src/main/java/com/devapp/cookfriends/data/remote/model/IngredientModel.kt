package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.Ingredient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class IngredientModel(
    @SerialName("id"          ) var id          : Uuid,
    @SerialName("name"        ) var name        : String,
    @SerialName("quantity"    ) var quantity    : String,
    @SerialName("measurement" ) var measurement : String? = null,
    @SerialName("recipeId"    ) var recipeId    : Uuid
)

fun Ingredient.toModel() = IngredientModel(id, name, quantity, measurement, recipeId)
