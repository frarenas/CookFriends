package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.RecipeType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class RecipeTypeModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("name"    ) var name      : String
)

fun RecipeType.toModel() = RecipeTypeModel(id, name)
