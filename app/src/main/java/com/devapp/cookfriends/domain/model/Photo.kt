package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.PhotoEntity
import com.devapp.cookfriends.data.remote.model.PhotoModel
import kotlin.uuid.Uuid

data class Photo(
    var id: Uuid = Uuid.random(),
    var url: String,
    var recipeId: Uuid
)

fun PhotoModel.toDomain() = Photo(id, url, recipeId)

fun PhotoEntity.toDomain() = Photo(id, url, recipeId)
