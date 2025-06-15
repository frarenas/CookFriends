package com.devapp.cookfriends.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class RecipeTypeModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("name"    ) var name      : String
)
