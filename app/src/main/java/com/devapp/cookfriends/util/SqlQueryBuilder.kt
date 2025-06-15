package com.devapp.cookfriends.util

import androidx.sqlite.db.SimpleSQLiteQuery
import com.devapp.cookfriends.domain.model.OrderBy
import com.devapp.cookfriends.domain.model.SearchOptions
import kotlin.uuid.Uuid

object SqlQueryBuilder {
    fun getProductsDynamically(userId: Uuid? = null, options: SearchOptions): SimpleSQLiteQuery {
        val queryBuilder = StringBuilder("""
            SELECT 
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

        // 1. Search Text and Keywords (using FTS)
        /*val ftsQueryParts = mutableListOf<String>()
        if (options.searchText.isNotBlank()) {
            // FTS simple match for general search text
            ftsQueryParts.add("name LIKE '${options.searchText}%'")
        }
        if (options.includedIngredients.isNotEmpty()) {
            val keywordFtsQueries = options.includedIngredients.joinToString(" AND ") { keyword ->
                // Use quotes for multi-word keywords if desired, and AND operator
                "name MATCH '$keyword*' OR description MATCH '$keyword*'"
            }
            ftsQueryParts.add("($keywordFtsQueries)")
        }

        if (ftsQueryParts.isNotEmpty()) {
            // Combine FTS parts with AND
            val combinedFtsQuery = ftsQueryParts.joinToString(" AND ")
            // Link main table to FTS table using rowid (which is ProductEntity's primary key)
            whereClauses.add("id IN (SELECT rowid FROM products_fts WHERE $combinedFtsQuery)")
        }*/


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