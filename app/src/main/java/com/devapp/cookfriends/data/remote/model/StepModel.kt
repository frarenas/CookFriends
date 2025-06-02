package com.devapp.cookfriends.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class StepModel @OptIn(ExperimentalUuidApi::class) constructor(
    @SerialName("id"      ) var id        : Uuid,
    @SerialName("order"   ) var order     : Int?    = null,
    @SerialName("content" ) var content   : String? = null,
    @SerialName("recipeId") var recipeId  : Uuid
)
