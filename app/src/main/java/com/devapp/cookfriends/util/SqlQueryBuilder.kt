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
                         WHERE ft.recipe_id = r.id AND ft.user_id = $userId)) AS isUserFavorite
            FROM recipe_table r
        """)
        val whereClauses = mutableListOf<String>()

        // Search word
        options.searchText.takeIf { it.isNotBlank() }?.let { searchText ->
            whereClauses.add("name LIKE '$searchText%'")
        }

        // Author
        options.author?.takeIf { it.isNotBlank() }?.let { author ->
            whereClauses.add("author LIKE '$author'")
        }

        // Recipe type
        options.recipeType?.takeIf { it.isNotBlank() }?.let { recipeType ->
            whereClauses.add("type = '$recipeType'")
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

        // Add WHERE clause if any filters are present
        if (whereClauses.isNotEmpty()) {
            queryBuilder.append("WHERE ")
            queryBuilder.append(whereClauses.joinToString(" AND "))
        }

        // Order By
        when (options.order) {
            OrderBy.NAME -> queryBuilder.append(" ORDER BY name ASC")
            OrderBy.AUTHOR -> queryBuilder.append(" ORDER BY author ASC, name ASC")
            OrderBy.DATE -> queryBuilder.append(" ORDER BY date ASC, name ASC")
        }

        val rawQuery = SimpleSQLiteQuery(queryBuilder.toString())

        return rawQuery
    }
}