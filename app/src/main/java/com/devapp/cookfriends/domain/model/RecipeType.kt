package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.RecipeTypeEntity
import com.devapp.cookfriends.data.remote.model.RecipeTypeModel
import kotlin.uuid.Uuid

data class RecipeType(
    var id: Uuid = Uuid.random(),
    var name: String
)

fun RecipeTypeModel.toDomain() = RecipeType(id, name)

fun RecipeTypeEntity.toDomain() = RecipeType(id, name)
