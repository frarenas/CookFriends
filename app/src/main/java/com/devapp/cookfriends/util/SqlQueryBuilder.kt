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


        options.searchText.takeIf { it.isNotBlank() }?.let { searchText ->
            whereClauses.add("name LIKE '$searchText%'")
        }

        // 2. Author Filter
        options.author?.takeIf { it.isNotBlank() }?.let { author ->
            whereClauses.add("author LIKE '$author'")
        }

        options.recipeType?.takeIf { it.isNotBlank() }?.let { recipeType ->
            whereClauses.add("type = '$recipeType'")
        }

        if (options.includedIngredients.isNotEmpty()) {
            options.includedIngredients.forEach { ingredientName ->
                val cleanIngredientName = ingredientName.replace("'", "''")

                whereClauses.add("""
                    EXISTS (SELECT 1 
                            FROM ingredient_table it 
                            WHERE it.recipe_id = r.id AND it.name LIKE '$cleanIngredientName')
                """.trimIndent())
            }
        }

        // Add WHERE clause if any filters are present
        if (whereClauses.isNotEmpty()) {
            queryBuilder.append("WHERE ")
            queryBuilder.append(whereClauses.joinToString(" AND "))
        }

        // 3. Order By
        when (options.order) {
            OrderBy.NAME -> queryBuilder.append(" ORDER BY name ASC")
            OrderBy.AUTHOR -> queryBuilder.append(" ORDER BY author ASC")
            OrderBy.DATE -> queryBuilder.append(" ORDER BY date ASC, name ASC")
        }

        val rawQuery = SimpleSQLiteQuery(queryBuilder.toString())

        return rawQuery
    }
}