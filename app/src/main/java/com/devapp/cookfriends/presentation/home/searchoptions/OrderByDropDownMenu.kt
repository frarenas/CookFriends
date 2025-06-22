package com.devapp.cookfriends.presentation.home.searchoptions

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
import com.devapp.cookfriends.domain.model.OrderBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderByDropDownMenu(
    selectedItem: OrderBy,
    availableItems: List<OrderBy>,
    onSelectItem: (OrderBy) -> Unit,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.label_order_by)
) {
    var menuExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = menuExpanded,
        onExpandedChange = { menuExpanded = !menuExpanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedItem.name,
            onValueChange = { },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
                .clickable(onClick = { menuExpanded = true }),
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text(selectedItem.name) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                cursorColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            availableItems.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(item.name)
                    },
                    onClick = {
                        onSelectItem(item)
                        menuExpanded = false
                    }
                )
            }
        }
    }
}
