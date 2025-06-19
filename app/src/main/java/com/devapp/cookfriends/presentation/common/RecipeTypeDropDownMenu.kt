package com.devapp.cookfriends.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.RecipeType

@Composable
fun RecipeTypeDropDownMenu(
    selectedRecipeType: RecipeType?,
    availableRecipeTypes: List<RecipeType>,
    onSelectItem: (RecipeType?) -> Unit
) {

    var selectedRecipeType by remember {
        mutableStateOf<RecipeType?>(selectedRecipeType)
    }
    val selectOneRecipeTypePlaceholder = stringResource(R.string.select_recipe_type)
    var recipeTypeMenuExpanded by remember { mutableStateOf(false) }

    Text(stringResource(R.string.label_recipe_type))
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    recipeTypeMenuExpanded = true
                }
                .padding(vertical = 8.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    MaterialTheme.shapes.small
                )
                .padding(horizontal = 16.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                selectedRecipeType?.name ?: selectOneRecipeTypePlaceholder
            )
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.label_recipe_type)
            )
        }

        DropdownMenu(
            expanded = recipeTypeMenuExpanded,
            onDismissRequest = { recipeTypeMenuExpanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            DropdownMenuItem(
                text = { Text(selectOneRecipeTypePlaceholder) },
                onClick = {
                    selectedRecipeType = null
                    onSelectItem(null)
                    recipeTypeMenuExpanded = false
                }
            )
            availableRecipeTypes.forEach { recipeType ->
                DropdownMenuItem(
                    onClick = {
                        selectedRecipeType = recipeType
                        onSelectItem(recipeType)
                        recipeTypeMenuExpanded = false
                    },
                    text = { Text(recipeType.name) },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
        }
    }
}
