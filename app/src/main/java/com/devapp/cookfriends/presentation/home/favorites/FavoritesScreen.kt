package com.devapp.cookfriends.presentation.home.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.home.Content
import com.devapp.cookfriends.presentation.home.Header
import com.devapp.cookfriends.presentation.home.searchoptions.SearchOptionsDialog

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel = hiltViewModel()) {

    val recipesState by viewModel.recipesState.collectAsState()
    val showSearchOptionsDialog by viewModel.showSearchOptionsDialog.collectAsState()
    val currentSearchOptions by viewModel.currentSearchOptions.collectAsState()
    val availableRecipeTypes by viewModel.availableRecipeTypes.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()) {
        Header(
            modifier = Modifier,
            title = stringResource(R.string.favorites),
            initialOptions = currentSearchOptions,
            onSearchClick = { newOptions ->
                viewModel.applySearchOptions(newOptions)
            },
            onSearchOptionsClick = { viewModel.openSearchOptionsDialog() }
        )
        Content(
            recipesState = recipesState,
            onFavoriteClick = {viewModel.toggleFavorite(it)}
        )
    }

    if (showSearchOptionsDialog) {
        SearchOptionsDialog(
            initialOptions = currentSearchOptions,
            availableRecipeTypes = availableRecipeTypes,
            onDismiss = { viewModel.dismissSearchOptionsDialog() },
            onApply = { newOptions ->
                viewModel.applySearchOptions(newOptions)
            }
        )
    }
}
