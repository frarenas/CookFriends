package com.devapp.cookfriends.data.remote.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.uuid.Uuid

@Serializable
data class RecipeModel(
    @SerialName("id"          ) var id          : Uuid,
    @SerialName("name"        ) var name        : String?                = null,
    @SerialName("description" ) var description : String?                = null,
    @SerialName("portions"    ) var portions    : Int?                   = null,
    @SerialName("userId"      ) var userId      : Uuid,
    @SerialName("typeId"      ) var typeId      : Uuid,
    @SerialName("author"      ) var author      : String?                = null,
    @SerialName("type"        ) var type        : String?                = null,
    @SerialName("ingredients" ) var ingredients : ArrayList<IngredientModel> = arrayListOf(),
    @SerialName("steps"       ) var steps       : ArrayList<StepModel>       = arrayListOf(),
    @SerialName("photos"      ) var photos      : ArrayList<PhotoModel>      = arrayListOf(),
    @SerialName("comments"    ) var comments    : ArrayList<CommentModel>    = arrayListOf(),
    @SerialName("ratings"     ) var ratings     : ArrayList<RatingModel>     = arrayListOf(),
    @SerialName("favorites"   ) var favorites   : ArrayList<FavoriteModel>   = arrayListOf(),
    @SerialName("date"        ) var date        : Instant
)
