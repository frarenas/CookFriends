package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.RecipePhotoEntity
import com.devapp.cookfriends.data.remote.model.RecipePhotoModel
import kotlin.uuid.Uuid

data class RecipePhoto(
    var id: Uuid = Uuid.random(),
    var url: String,
    var recipeId: Uuid
)

fun RecipePhotoModel.toDomain() = RecipePhoto(id, url, recipeId)

fun RecipePhotoEntity.toDomain() = RecipePhoto(id, url, recipeId)
