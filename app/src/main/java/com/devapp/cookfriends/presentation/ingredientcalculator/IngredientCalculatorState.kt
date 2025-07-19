package com.devapp.cookfriends.presentation.ingredientcalculator

import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.model.UiMessage

data class IngredientCalculatorState(
    val ingredients: List<Ingredient> = emptyList(),
    val originalPortions: Int = 1,
    val desiredPortions: Int = 1,
    val adjustedIngredients: List<Ingredient> = emptyList(),
    val message: UiMessage? = null
)
