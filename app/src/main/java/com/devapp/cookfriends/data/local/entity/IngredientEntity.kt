package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devapp.cookfriends.domain.model.Ingredient
import kotlin.uuid.Uuid

@Entity(
    tableName = "ingredient_table",
    foreignKeys = [ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recipe_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class IngredientEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "quantity") val quantity: String? = null,
    @ColumnInfo(name = "measurement") val measurement: String? = null,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: Uuid
)

fun Ingredient.toDatabase() = IngredientEntity(
    id = id,
    name = name,
    quantity = quantity,
    measurement = measurement,
    recipeId = recipeId
)
