package com.devapp.cookfriends.presentation.home.searchoptions

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.OrderBy
import com.devapp.cookfriends.domain.model.RecipeType
import com.devapp.cookfriends.domain.model.SearchOptions

@Composable
fun SearchOptionsDialog(
    initialOptions: SearchOptions,
    availableRecipeTypes: List<RecipeType>,
    onDismiss: () -> Unit,
    onApply: (SearchOptions) -> Unit
) {
    var currentSearchText by remember { mutableStateOf(initialOptions.searchText) }
    var currentAuthor by remember {
        mutableStateOf(
            initialOptions.author ?: ""
        )
    }
    var includedIngredients by remember { mutableStateOf(initialOptions.includedIngredients.toList()) }
    var excludedIngredients by remember { mutableStateOf(initialOptions.excludedIngredients.toList()) }

    var currentOrder by remember { mutableStateOf(initialOptions.order) }
    var selectedRecipeType by remember {
        mutableStateOf(availableRecipeTypes.find { it.name == initialOptions.recipeType })
    }

    var orderMenuExpanded by remember { mutableStateOf(false) }
    var recipeTypeMenuExpanded by remember { mutableStateOf(false) }

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
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.advance_search),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Search Text input field
                OutlinedTextField(
                    value = currentSearchText,
                    onValueChange = { currentSearchText = it },
                    label = { Text("Search Text") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Recipe type
                Box(modifier = Modifier.fillMaxWidth()) { // Use Box to anchor DropdownMenu
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                recipeTypeMenuExpanded = true
                            } // Make the whole row clickable
                            .padding(vertical = 8.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                MaterialTheme.shapes.small
                            ) // Visual border like OutlinedTextField
                            .padding(horizontal = 16.dp), // Padding inside the "text field" part
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            selectedRecipeType?.name ?: ""
                        )
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown for recipe type"
                        )
                    }

                    // The actual dropdown menu
                    DropdownMenu(
                        expanded = recipeTypeMenuExpanded,
                        onDismissRequest = { recipeTypeMenuExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        availableRecipeTypes.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedRecipeType = category
                                    recipeTypeMenuExpanded = false
                                },
                                text = { Text(category.name) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Author
                OutlinedTextField(
                    value = currentAuthor,
                    onValueChange = { currentAuthor = it },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Included ingredients
                TagSelector(
                    label = stringResource(R.string.included_ingeredients),
                    currentTags = includedIngredients,
                    onTagsChange = { includedIngredients = it })

                // Excluded ingredients
                TagSelector(
                    label = stringResource(R.string.excluded_ingeredients),
                    currentTags = excludedIngredients,
                    onTagsChange = { excludedIngredients = it })

                // Order By Dropdown Menu
                Box(modifier = Modifier.fillMaxWidth()) { // Use Box to anchor DropdownMenu
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { orderMenuExpanded = true } // Make the whole row clickable
                            .padding(vertical = 8.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                MaterialTheme.shapes.small
                            ) // Visual border like OutlinedTextField
                            .padding(horizontal = 16.dp), // Padding inside the "text field" part
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Order By: ${currentOrder.name.replace("_", " ")}",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (currentOrder == OrderBy.NAME) 0.6f else 1f)
                        ) // Hint like color for default
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown for order by"
                        )
                    }

                    // The actual dropdown menu
                    DropdownMenu(
                        expanded = orderMenuExpanded,
                        onDismissRequest = { orderMenuExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f) // Adjust width to match the clickable row
                    ) {
                        OrderBy.entries.forEach { order ->
                            DropdownMenuItem(
                                text = {
                                    Text(order.name.replace("_", " "))
                                },
                                onClick = {
                                    currentOrder = order
                                    orderMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons (Cancel and Apply)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround // Distribute buttons evenly
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            // Create the SearchOptions object and pass it via callback
                            onApply(
                                SearchOptions(
                                    searchText = currentSearchText,
                                    recipeType = selectedRecipeType?.name,
                                    includedIngredients = includedIngredients,
                                    excludedIngredients = excludedIngredients,
                                    author = currentAuthor.takeIf { it.isNotBlank() }, // Set to null if author field is blank
                                    order = currentOrder
                                )
                            )
                        }
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}