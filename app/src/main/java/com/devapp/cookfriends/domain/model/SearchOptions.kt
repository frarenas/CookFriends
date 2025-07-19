package com.devapp.cookfriends.domain.model

import kotlin.uuid.Uuid

data class SearchOptions(
    val searchText: String = "",
    val username: String? = null,
    val recipeType: RecipeType? = null,
    val includedIngredients: List<String> = emptyList(),
    val excludedIngredients: List<String> = emptyList(),
    val onlyFavorites: Boolean = false,
    val currentUserId: Uuid? = null,
    val userCalculated: Boolean = false,
    val order: OrderBy = OrderBy.NAME,
    val limit: Int = Int.MAX_VALUE
)

enum class OrderBy {
    NAME,
    DATE,
    AUTHOR
}
