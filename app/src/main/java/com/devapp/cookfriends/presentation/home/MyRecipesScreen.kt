package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MyRecipesScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val homeState by viewModel.homeState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()) {
        Header(
            modifier = Modifier,
            title = "Mis Recetas"
        )
        LazyColumn {
            items(homeState.recipeList) { item ->
                RecipeListItem(item)
            }
        }
    }
}
