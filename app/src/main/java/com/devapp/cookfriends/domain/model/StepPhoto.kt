package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.StepPhotoEntity
import com.devapp.cookfriends.data.remote.model.StepPhotoModel
import kotlin.uuid.Uuid

data class StepPhoto(
    var id: Uuid = Uuid.random(),
    var url: String,
    var stepId: Uuid
)

fun StepPhotoModel.toDomain() = StepPhoto(id, url, stepId)

fun StepPhotoEntity.toDomain() = StepPhoto(id, url, stepId)
