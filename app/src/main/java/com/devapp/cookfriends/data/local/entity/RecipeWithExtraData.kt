package com.devapp.cookfriends.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.devapp.cookfriends.domain.model.Recipe

data class RecipeWithExtraData(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val ingredients: List<IngredientEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val steps: List<StepEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val photos: List<PhotoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val ratings: List<RatingEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val comments: List<CommentEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val favorites: List<FavoriteEntity>,
    val averageRating: Double? = null,
    val isUserFavorite: Boolean = false
)

fun Recipe.toDatabase() = RecipeWithExtraData(
    recipe = RecipeEntity(
        id = id,
        name = name,
        author = author,
        type = type,
        portions = portions,
        date = date
    ),
    ingredients = ingredients.map { ingredient -> ingredient.toDatabase() },
    steps = steps.map { step -> step.toDatabase() },
    photos = photos.map { photo -> photo.toDatabase() },
    ratings = ratings.map { rating -> rating.toDatabase() },
    comments = comments.map { comment -> comment.toDatabase() },
    favorites = favorites.map { favorite -> favorite.toDatabase() }
)
