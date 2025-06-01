package com.devapp.cookfriends.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StepModel (
    @SerialName("id"      ) var id        : Int?    = null,
    @SerialName("order"   ) var order     : Int?    = null,
    @SerialName("content" ) var content   : String? = null,
    @SerialName("recipeId") var recipeId  : Int?    = null
)
