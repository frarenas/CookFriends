package com.devapp.cookfriends.presentation.home.searchoptions

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.devapp.cookfriends.domain.model.OrderBy
import com.devapp.cookfriends.domain.model.SearchOptions

@Composable
fun SearchOptionsDialog(
    initialOptions: SearchOptions, // Pass current options to pre-fill the dialog fields
    onDismiss: () -> Unit, // Callback when the dialog is dismissed (e.g., Cancel button, outside click)
    onApply: (SearchOptions) -> Unit // Callback when the "Apply" button is clicked
) {
    // Internal state for the dialog's fields, initialized with current options
    var currentSearchText by remember { mutableStateOf(initialOptions.searchText) }
    var currentAuthor by remember {
        mutableStateOf(
            initialOptions.author ?: ""
        )
    }
    var currentKeywords by remember { mutableStateOf(initialOptions.includedIngredients.toMutableList()) }
    var newKeywordInput by remember { mutableStateOf("") }
    var currentOrder by remember { mutableStateOf(initialOptions.order) }

    // State for the OrderBy dropdown menu visibility
    var orderMenuExpanded by remember { mutableStateOf(false) }

    // Use the standard Dialog composable
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
                horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
            ) {
                Text(
                    "Search Options",
                    //style = MaterialTheme.typography.h6,
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

                // Author input field (optional)
                OutlinedTextField(
                    value = currentAuthor,
                    onValueChange = { currentAuthor = it },
                    label = { Text("Author (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newKeywordInput,
                    onValueChange = { newKeywordInput = it },
                    label = { Text("Add Keywords") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (newKeywordInput.isNotBlank()) {
                            currentKeywords.add(newKeywordInput.trim())
                            newKeywordInput = "" // Clear input field
                        }
                    })
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Included ingredients
                if (currentKeywords.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        currentKeywords.forEach { keyword ->
                            FilterChip(
                                onClick = {
                                    currentKeywords.remove(keyword)
                                },
                                label = {
                                    Text(keyword)
                                },
                                selected = false
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

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