package com.devapp.cookfriends.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Step (
    @SerialName("order"   ) var order   : Int? = null,
    @SerialName("content" ) var content : String? = null
)
