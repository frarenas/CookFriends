package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.StepWithPhotos
import com.devapp.cookfriends.data.remote.model.StepModel
import kotlin.uuid.Uuid

data class Step(
    var id: Uuid = Uuid.random(),
    var order: Int,
    var content: String,
    var recipeId: Uuid,
    var photos: List<StepPhoto> = arrayListOf()
)

fun StepModel.toDomain() = Step(
    id,
    order,
    content,
    recipeId,
    photos.map { photo -> photo.toDomain() }
)

fun StepWithPhotos.toDomain() = Step(
    step.id,
    step.order,
    step.content,
    step.recipeId,
    photos.map { photo -> photo.toDomain() }
)
