package com.devapp.cookfriends.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.RecipeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeTypeDropDownMenu(
    selectedRecipeType: RecipeType?,
    availableRecipeTypes: List<RecipeType>,
    onSelectItem: (RecipeType?) -> Unit,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.label_recipe_type)
) {
    val selectOneRecipeTypePlaceholder = stringResource(R.string.select_recipe_type)
    var recipeTypeMenuExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = recipeTypeMenuExpanded,
        onExpandedChange = { recipeTypeMenuExpanded = !recipeTypeMenuExpanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedRecipeType?.name ?: "",
            onValueChange = { },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
                .clickable(onClick = { recipeTypeMenuExpanded = true }),
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text(selectOneRecipeTypePlaceholder) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = recipeTypeMenuExpanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                cursorColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = recipeTypeMenuExpanded,
            onDismissRequest = { recipeTypeMenuExpanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuItem(
                text = { Text(selectOneRecipeTypePlaceholder) },
                modifier = Modifier.fillMaxWidth(0.9f),
                onClick = {
                    onSelectItem(null)
                    recipeTypeMenuExpanded = false
                }
            )
            availableRecipeTypes.forEach { recipeType ->
                DropdownMenuItem(
                    text = { Text(recipeType.name) },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    onClick = {
                        onSelectItem(recipeType)
                        recipeTypeMenuExpanded = false
                    }
                )
            }
        }
    }
}
