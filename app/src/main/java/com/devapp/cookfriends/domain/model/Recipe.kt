package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.local.entity.RecipeWithExtraData
import com.devapp.cookfriends.data.remote.model.RecipeModel
import kotlin.uuid.Uuid

data class Recipe(
    var id: Uuid = Uuid.random(),
    var name: String? = null,
    var author: String? = null,
    var rating: Double? = null,
    var favorite: Boolean? = null,
    var type: String? = null,
    var portions: Int? = null,
    var ingredients: List<Ingredient> = arrayListOf(),
    var steps: List<Step> = arrayListOf(),
    var photos: List<Photo> = arrayListOf(),
    var comments: List<Comment> = arrayListOf(),
    var ratings: List<Rating> = arrayListOf()
)

fun RecipeModel.toDomain() = Recipe(
    id,
    name,
    author,
    null,
    null,
    type,
    portions,
    ingredients.map { ingredient -> ingredient.toDomain() },
    steps.map { step -> step.toDomain() },
    photos.map { photo -> photo.toDomain() },
    comments.map { comment -> comment.toDomain() },
    ratings.map { rating -> rating.toDomain() }
)

fun RecipeWithExtraData.toDomain() = Recipe(
    recipe.id,
    recipe.name,
    recipe.author,
    averageRating,
    null,
    recipe.type,
    recipe.portions,
    ingredients.map { ingredient -> ingredient.toDomain() },
    steps.map { step -> step.toDomain() },
    photos.map { photo -> photo.toDomain() },
    comments.map { comment -> comment.toDomain() },
    ratings.map { rating -> rating.toDomain() }
)
