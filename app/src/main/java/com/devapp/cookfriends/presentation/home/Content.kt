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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.presentation.common.MessageScreen
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import kotlin.uuid.Uuid

@Composable
fun Content(
    isUserLogged: Boolean = false,
    recipesState: RecipesState = RecipesState(),
    onFavoriteClick: (Uuid) -> Unit,
    onDeleteClick: (Uuid) -> Unit = { },
    onItemClick: (Uuid) -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        if (recipesState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (recipesState.message != null) {
            MessageScreen(
                message = recipesState.message.uiText.asString(context),
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
                    items(recipesState.recipeList) { recipe ->
                        RecipeListItem(
                            recipe = recipe,
                            isUserLogged = isUserLogged,
                            onFavoriteClick = { recipeId ->
                                onFavoriteClick(recipeId)
                            },
                            onDeleteClick = { recipeId ->
                                onDeleteClick(recipeId)
                            },
                            onItemClick = { recipeId ->
                                onItemClick(recipeId)
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
        Content(
            recipesState = RecipesState(isLoading = true),
            onFavoriteClick = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreviewError() {
    CookFriendsTheme {
        Content(
            recipesState = RecipesState(
                message = UiMessage(
                    UiText.StringResource(R.string.generic_error),
                    blocking = true
                )
            ),
            onFavoriteClick = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreviewEmpty() {
    CookFriendsTheme {
        Content(
            recipesState = RecipesState(recipeList = emptyList()),
            onFavoriteClick = {},
            onItemClick = {}
        )
    }
}
