package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.RecipePhoto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class RecipePhotoModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("url"     ) var url       : String,
    @SerialName("recipeId") var recipeId  : Uuid
)

fun RecipePhoto.toModel() = RecipePhotoModel(id, url, recipeId)
