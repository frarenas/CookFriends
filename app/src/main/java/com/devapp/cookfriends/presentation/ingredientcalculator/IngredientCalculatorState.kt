package com.devapp.cookfriends.presentation.ingredientcalculator

import com.devapp.cookfriends.domain.model.Ingredient

data class IngredientCalculatorState(
    val ingredients: List<Ingredient> = emptyList(),
    val originalPortions: Int = 1,
    val desiredPortions: Int = 1,
    val adjustedIngredients: List<Ingredient> = emptyList()
)
