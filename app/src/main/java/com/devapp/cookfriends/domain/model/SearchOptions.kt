package com.devapp.cookfriends.domain.model

data class SearchOptions(
    val searchText: String = "",
    val username: String? = null,
    val recipeType: RecipeType? = null,
    val includedIngredients: List<String> = emptyList(),
    val excludedIngredients: List<String> = emptyList(),
    val onlyFavorites: Boolean = false,
    val order: OrderBy = OrderBy.NAME
)

enum class OrderBy {
    NAME,
    DATE,
    AUTHOR
}
