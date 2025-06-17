package com.devapp.cookfriends.presentation.home.searchoptions

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.R

@Composable
fun TagSelector(
    label: String = "Select tags",
    currentTags: List<String>,
    onTagsChange: (List<String>) -> Unit
) {
    var selectedTags = remember { mutableStateListOf<String>().apply { addAll(currentTags) } }
    var newTagInput by remember { mutableStateOf("") }

    fun addTag() {
        if (newTagInput.isNotBlank()) {
            val tagToAdd = newTagInput.trim()
            if (!selectedTags.contains(tagToAdd)) {
                selectedTags.add(tagToAdd)
                onTagsChange(selectedTags.toList())
            }
            newTagInput = ""
        }
    }

    OutlinedTextField(
        value = newTagInput,
        onValueChange = { newTagInput = it },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { addTag()} ),
        trailingIcon = {
            if (newTagInput.isNotBlank()) {
                IconButton(onClick = { addTag() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
            }
        }
    )
    Spacer(modifier = Modifier.height(8.dp))

    if (selectedTags.isNotEmpty()) {
        FlowRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            selectedTags.forEach { tag ->
                FilterChip(
                    onClick = {
                        selectedTags.remove(tag)
                        onTagsChange(selectedTags)
                    },
                    label = {
                        Text(tag)
                    },
                    selected = false,
                    trailingIcon = {
                        Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = stringResource(R.string.delete, tag)
                    )},
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
