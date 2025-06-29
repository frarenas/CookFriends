package com.devapp.cookfriends.presentation.recipeDetail

import com.devapp.cookfriends.domain.model.Step

data class RecipeDetailState(
    val recipeName: String = "Receta",
    val score: Double = 5.0,
    val ingredients: List<String> = emptyList(),
    val stepList: List<Step> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
