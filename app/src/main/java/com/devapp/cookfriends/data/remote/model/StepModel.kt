package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.Step
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class StepModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("order"   ) var order     : Int,
    @SerialName("content" ) var content   : String,
    @SerialName("recipeId") var recipeId  : Uuid,
    @SerialName("photos"  ) var photos    : List<StepPhotoModel> = arrayListOf()
)

fun Step.toModel() = StepModel(
    id,
    order,
    content,
    recipeId,
    photos.map { photo -> photo.toModel() }
)
