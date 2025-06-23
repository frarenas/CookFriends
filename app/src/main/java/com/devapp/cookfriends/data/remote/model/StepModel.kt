package com.devapp.cookfriends.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class StepModel(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("order"   ) var order     : Int,
    @SerialName("content" ) var content   : String,
    @SerialName("recipeId") var recipeId  : Uuid,
    @SerialName("photos"  ) var photos    : ArrayList<StepPhotoModel> = arrayListOf()
)
