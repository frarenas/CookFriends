package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.RecipeWithExtraData
import com.devapp.cookfriends.data.remote.model.RecipeModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.String
import kotlin.uuid.Uuid

data class Recipe(
    var id: Uuid = Uuid.random(),
    var name: String? = null,
    var description: String? = null,
    var user: User? = null,
    var averageRating: Double? = null,
    var isUserFavorite: Boolean = false,
    var recipeType: RecipeType? = null,
    var portions: Int? = null,
    var ingredients: List<Ingredient> = arrayListOf(),
    var steps: List<Step> = arrayListOf(),
    var recipePhotos: List<RecipePhoto> = arrayListOf(),
    var comments: List<Comment> = arrayListOf(),
    var ratings: List<Rating> = arrayListOf(),
    var favorites: List<Favorite> = arrayListOf(),
    var date: Instant = Clock.System.now(),
    var userCalculated: Boolean = false,
    var updatePending: Boolean = false,
    var published: Boolean = false
)

fun RecipeModel.toDomain() = Recipe(
    id = id,
    name = name,
    description = description,
    user = user.toDomain(),
    recipeType = recipeType.toDomain(),
    portions = portions,
    ingredients = ingredients.map { ingredient -> ingredient.toDomain() },
    steps = steps.map { step -> step.toDomain() },
    recipePhotos = recipePhotos.map { photo -> photo.toDomain() },
    comments = comments.map { comment -> comment.toDomain() },
    ratings = ratings.map { rating -> rating.toDomain() },
    favorites = favorites.map { favorite -> favorite.toDomain() },
    date = date,
    userCalculated = userCalculated,
    published = published
)

fun RecipeWithExtraData.toDomain() = Recipe(
    recipe.id,
    recipe.name,
    recipe.description,
    user?.toDomain(),
    averageRating,
    isUserFavorite,
    recipeType?.toDomain(),
    recipe.portions,
    ingredients.map { ingredient -> ingredient.toDomain() },
    steps.map { step -> step.toDomain() },
    photos.map { photo -> photo.toDomain() },
    comments.map { comment -> comment.toDomain() },
    ratings.map { rating -> rating.toDomain() },
    favorites.map { favorite -> favorite.toDomain() },
    date = recipe.date,
    userCalculated = recipe.userCalculated,
    published = recipe.published
)
