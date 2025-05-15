package com.devapp.cookfriends.domain.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Recipe(
    @SerialName("name"        ) var name        : String?                = null,
    @SerialName("author"      ) var author      : String?                = null,
    @SerialName("rate"        ) var rate        : Double?                = null,
    @SerialName("favorite"    ) var favorite    : Boolean?                = null,
    @SerialName("type"        ) var type        : String?                = null,
    @SerialName("portions"    ) var portions    : Int?                   = null,
    @SerialName("ingredients" ) var ingredients : ArrayList<Ingredient> = arrayListOf(),
    @SerialName("steps"       ) var steps       : ArrayList<Step>       = arrayListOf()
)
