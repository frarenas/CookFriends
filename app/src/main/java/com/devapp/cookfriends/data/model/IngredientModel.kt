package com.devapp.cookfriends.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientModel(
    @SerialName("id"          ) var id          : Int?    = null,
    @SerialName("name"        ) var name        : String? = null,
    @SerialName("quantity"    ) var quantity    : String? = null,
    @SerialName("measurement" ) var measurement : String? = null,
    @SerialName("recipeId"    ) var recipeId    : Int?    = null
)
