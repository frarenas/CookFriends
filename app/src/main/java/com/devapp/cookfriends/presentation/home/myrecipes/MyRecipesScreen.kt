package com.devapp.cookfriends.presentation.home.myrecipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import kotlin.uuid.Uuid

@Composable
fun MyRecipesScreen(
    isUserLogged: Boolean = false,
    navigateToDetail: (recipeId: Uuid) -> Unit,
    viewModel: MyRecipesViewModel = hiltViewModel()
) {

    val recipesState by viewModel.recipesState.collectAsState()
    val showSearchOptionsDialog by viewModel.showSearchOptionsDialog.collectAsState()
    val currentSearchOptions by viewModel.currentSearchOptions.collectAsState()
    val availableRecipeTypes by viewModel.availableRecipeTypes.collectAsState()
    val tabs = listOf(stringResource(R.string.owned), stringResource(R.string.calculated))
    val selectedTab = viewModel.selectedTab.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header(
            modifier = Modifier,
            title = stringResource(R.string.my_recipes),
            initialOptions = currentSearchOptions,
            onSearchClick = { newOptions ->
                viewModel.applySearchOptions(newOptions)
            },
            onSearchOptionsClick = { viewModel.openSearchOptionsDialog() }
        )
        TabRow(selectedTabIndex = selectedTab.value) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.value == index,
                    onClick = {
                        viewModel.setSelectedTab(index)
                    },
                    text = { Text(text = title) },
                )
            }
        }
        Content(
            recipesState = recipesState,
            isUserLogged = isUserLogged,
            onFavoriteClick = { viewModel.toggleFavorite(it) },
            onDeleteClick = { viewModel.deleteRecipe(it) },
            onItemClick = { recipeId ->
                navigateToDetail(recipeId)
            }
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
