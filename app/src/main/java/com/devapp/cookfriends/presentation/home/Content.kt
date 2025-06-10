package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Content(recipesState: RecipesState = RecipesState()) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (recipesState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (recipesState.error != null) {
            Text(text = "Error: ${recipesState.error}", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(recipesState.recipeList) { item ->
                    RecipeListItem(
                        item,
                        onFavoriteClick = { clickedRecipe ->
                            // Call ViewModel
                        }
                    )
                }
            }
        }
    }
}
