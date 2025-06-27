package com.devapp.cookfriends.data.remote.model

import com.devapp.cookfriends.domain.model.Recipe
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.uuid.Uuid

@Serializable
data class RecipeModel(
    @SerialName("id"             ) var id            : Uuid,
    @SerialName("name"           ) var name          : String?                     = null,
    @SerialName("description"    ) var description   : String?                     = null,
    @SerialName("portions"       ) var portions      : Int?                        = null,
    @SerialName("user"           ) var user          : UserModel,
    @SerialName("recipeType"     ) var recipeType    : RecipeTypeModel,
    @SerialName("ingredients"    ) var ingredients   : List<IngredientModel>       = arrayListOf(),
    @SerialName("steps"          ) var steps         : List<StepModel>             = arrayListOf(),
    @SerialName("photos"         ) var recipePhotos  : List<RecipePhotoModel>      = arrayListOf(),
    @SerialName("comments"       ) var comments      : List<CommentModel>          = arrayListOf(),
    @SerialName("ratings"        ) var ratings       : List<RatingModel>           = arrayListOf(),
    @SerialName("favorites"      ) var favorites     : List<FavoriteModel>         = arrayListOf(),
    @SerialName("date"           ) var date          : Instant,
    @SerialName("user_calculated") var userCalculated: Boolean                     = false,
    @SerialName("update_pending" ) var updatePending : Boolean                     = false
)

fun Recipe.toModel() = RecipeModel(
    id = id,
    name = name,
    description = description,
    portions = portions,
    user = user!!.toModel(),
    recipeType = recipeType!!.toModel(),
    ingredients = ingredients.map { ingredient -> ingredient.toModel() },
    steps = steps.map { step -> step.toModel() },
    recipePhotos = recipePhotos.map { photo -> photo.toModel() },
    date = date,
    userCalculated = userCalculated,
    updatePending = updatePending
)
