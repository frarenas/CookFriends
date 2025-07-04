package com.devapp.cookfriends.util

import androidx.sqlite.db.SimpleSQLiteQuery
import com.devapp.cookfriends.domain.model.OrderBy
import com.devapp.cookfriends.domain.model.SearchOptions
import kotlin.uuid.Uuid

object SqlQueryBuilder {
    fun getRecipesDynamically(userId: Uuid? = null, options: SearchOptions): SimpleSQLiteQuery {
        val queryBuilder = StringBuilder("""
            SELECT DISTINCT
                r.*,
                (SELECT ROUND(AVG(rt.rate), 2) 
                 FROM rating_table rt 
                 WHERE rt.recipe_id = r.id) AS averageRating,
                (EXISTS (SELECT 1 
                         FROM favorite_table ft 
                         WHERE ft.recipe_id = r.id AND ft.user_id = '$userId')) AS isUserFavorite
            FROM recipe_table r
            LEFT JOIN user_table u ON r.user_id = u.id
        """)
        if (options.recipeType != null) {
            queryBuilder.append(" LEFT JOIN recipe_type_table rtt ON r.recipe_type_id = rtt.id ")
        }
        val whereClauses = mutableListOf<String>()

        // Published or user's own recipes
        whereClauses.add("(r.user_id = '$userId' OR r.published = 1)")

        // Search word
        options.searchText.takeIf { it.isNotBlank() }?.let { searchText ->
            whereClauses.add("r.name LIKE '$searchText%'")
        }

        // Author
        options.username?.takeIf { it.isNotBlank() }?.let { username ->
            whereClauses.add("u.username LIKE '$username%'")
        }

        // Recipe type
        options.recipeType?.let { recipeType ->
            whereClauses.add("rtt.id = '${recipeType.id}'")
        }

        // included ingredients
        if (options.includedIngredients.isNotEmpty()) {
            options.includedIngredients.forEach { ingredientName ->
                val cleanIngredientName = ingredientName.replace("'", "''")

                whereClauses.add("""
                    EXISTS (SELECT 1 
                            FROM ingredient_table it 
                            WHERE it.recipe_id = r.id AND LOWER(it.name) = LOWER('$cleanIngredientName'))
                """.trimIndent())
            }
        }

        // excluded ingredients
        if (options.excludedIngredients.isNotEmpty()) {
            options.excludedIngredients.forEach { ingredientName ->
                val cleanIngredientName = ingredientName.replace("'", "''") // Basic single quote escaping
                whereClauses.add("""
                    NOT EXISTS (SELECT 1 
                                FROM ingredient_table it 
                                WHERE it.recipe_id = r.id AND LOWER(it.name) = LOWER('$cleanIngredientName'))
                """.trimIndent()) // Using LOWER for case-insensitive matching
            }
        }

        // Only get favorites
        if(options.onlyFavorites) {
            whereClauses.add("isUserFavorite = 1")
        }

        // Only my recipes
        options.currentUserId?.let {
            whereClauses.add("r.user_id = '$it'")
        }

        // Add WHERE clause if any filters are present
        if (whereClauses.isNotEmpty()) {
            queryBuilder.append("WHERE ")
            queryBuilder.append(whereClauses.joinToString(" AND "))
        }

        // Order By
        when (options.order) {
            OrderBy.NAME -> queryBuilder.append(" ORDER BY name ASC")
            OrderBy.AUTHOR -> queryBuilder.append(" ORDER BY u.username ASC, name ASC")
            OrderBy.DATE -> queryBuilder.append(" ORDER BY date DESC, name ASC")
        }

        val rawQuery = SimpleSQLiteQuery(queryBuilder.toString())

        return rawQuery
    }
}
