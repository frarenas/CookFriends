package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.StepPhoto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class StepPhotoModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("url"     ) var url       : String,
    @SerialName("stepId"  ) var stepId    : Uuid
)

fun StepPhoto.toModel() = StepPhotoModel(id, url, stepId)
