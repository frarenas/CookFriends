package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.NoMeals
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.common.MessageScreen
import com.devapp.cookfriends.ui.theme.CookFriendsTheme

@Composable
fun Content(recipesState: RecipesState = RecipesState()) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (recipesState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (recipesState.error != null) {
            MessageScreen(
                message = recipesState.error,
                imageVector = Icons.Default.Error
            )
        } else {
            if (recipesState.recipeList.isEmpty()) {
                MessageScreen(
                    message = R.string.no_recipes_found,
                    imageVector = Icons.Default.NoMeals
                )
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
}

@Preview(showBackground = true)
@Composable
fun ContentPreviewLoading() {
    CookFriendsTheme {
        Content(RecipesState(isLoading = true))
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreviewError() {
    CookFriendsTheme {
        Content(RecipesState(error = "Error"))
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreviewEmpty() {
    CookFriendsTheme {
        Content(RecipesState(recipeList = emptyList()))
    }
}
