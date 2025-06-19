package com.devapp.cookfriends.presentation.editrecipe

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.devapp.cookfriends.R

@Composable
fun DeleteConfirmationDialog(
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.confirm_delete_title)) },
        text = { Text(text = stringResource(R.string.confirm_delete_message)) },
        confirmButton = {
            TextButton(onClick = {
                onConfirmDelete()
                onDismiss()
            }) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
