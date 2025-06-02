package com.devapp.cookfriends.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class IngredientModel @OptIn(ExperimentalUuidApi::class) constructor(
    @SerialName("id"          ) var id          : Uuid,
    @SerialName("name"        ) var name        : String? = null,
    @SerialName("quantity"    ) var quantity    : String? = null,
    @SerialName("measurement" ) var measurement : String? = null,
    @SerialName("recipeId"    ) var recipeId    : Uuid
)
