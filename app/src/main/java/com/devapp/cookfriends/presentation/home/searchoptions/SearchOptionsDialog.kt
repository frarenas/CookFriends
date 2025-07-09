package com.devapp.cookfriends.presentation.home.searchoptions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.OrderBy
import com.devapp.cookfriends.domain.model.RecipeType
import com.devapp.cookfriends.domain.model.SearchOptions
import com.devapp.cookfriends.presentation.common.RecipeTypeDropDownMenu

@Composable
fun SearchOptionsDialog(
    initialOptions: SearchOptions,
    availableRecipeTypes: List<RecipeType>,
    onDismiss: () -> Unit,
    onApply: (SearchOptions) -> Unit
) {
    var currentSearchText by remember { mutableStateOf(initialOptions.searchText) }
    var currentUsername by remember {
        mutableStateOf(
            initialOptions.username ?: ""
        )
    }
    var includedIngredients by remember { mutableStateOf(initialOptions.includedIngredients.toList()) }
    var excludedIngredients by remember { mutableStateOf(initialOptions.excludedIngredients.toList()) }
    var currentOrder by remember { mutableStateOf(initialOptions.order) }
    var selectedRecipeType by remember {
        mutableStateOf(initialOptions.recipeType)
    }

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.advance_search),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Search text
                OutlinedTextField(
                    value = currentSearchText,
                    onValueChange = { currentSearchText = it },
                    label = { Text(stringResource(R.string.search)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Recipe type
                RecipeTypeDropDownMenu(
                    selectedRecipeType,
                    availableRecipeTypes = availableRecipeTypes,
                    onSelectItem = { recipeType ->
                        selectedRecipeType = recipeType
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Author
                OutlinedTextField(
                    value = currentUsername,
                    onValueChange = { currentUsername = it },
                    label = { Text(stringResource(R.string.label_author)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Included ingredients
                TagSelector(
                    label = stringResource(R.string.included_ingredients),
                    currentTags = includedIngredients
                ) {
                    includedIngredients = it
                }

                // Excluded ingredients
                TagSelector(
                    label = stringResource(R.string.excluded_ingredients),
                    currentTags = excludedIngredients
                ) {
                    excludedIngredients = it
                }

                // Order By Dropdown Menu
                OrderByDropDownMenu(
                    selectedItem = currentOrder,
                    availableItems = OrderBy.entries,
                    onSelectItem = { order ->
                        currentOrder = order
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Search Button
                Button(
                    onClick = {
                        onApply(
                            SearchOptions(
                                searchText = currentSearchText,
                                recipeType = selectedRecipeType,
                                includedIngredients = includedIngredients,
                                excludedIngredients = excludedIngredients,
                                username = currentUsername.takeIf { it.isNotBlank() },
                                order = currentOrder
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.search))
                }
            }
        }
    }
}
