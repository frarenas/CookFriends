package com.devapp.cookfriends.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    @SerialName("name"        ) var name        : String? = null,
    @SerialName("quantity"    ) var quantity    : String? = null,
    @SerialName("measurement" ) var measurement : String? = null
)
