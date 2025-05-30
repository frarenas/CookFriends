package com.devapp.cookfriends.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RecipeModel(
    @SerialName("id"          ) var id          : Int?                   = null,
    @SerialName("name"        ) var name        : String?                = null,
    @SerialName("description"        ) var description        : String?                = null,
    @SerialName("portions"    ) var portions    : Int?                   = null,
    @SerialName("userId"    ) var userId    : Int?                   = null,
    @SerialName("TypeId"    ) var typeId    : Int?                   = null,
    @SerialName("author"      ) var author      : String?                = null,
    @SerialName("type"        ) var type        : String?                = null,
    //@SerialName("rate"        ) var rate        : Double?                = null,
    //@SerialName("favorite"    ) var favorite    : Boolean?                = null,
    @SerialName("ingredients" ) var ingredients : ArrayList<IngredientModel> = arrayListOf(),
    @SerialName("steps"       ) var steps       : ArrayList<StepModel>       = arrayListOf()
)
