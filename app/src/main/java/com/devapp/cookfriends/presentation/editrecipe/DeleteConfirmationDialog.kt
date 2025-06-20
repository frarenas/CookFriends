package com.devapp.cookfriends.presentation.editrecipe

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.devapp.cookfriends.R
import com.devapp.cookfriends.ui.theme.CookFriendsTheme

@Composable
fun DeleteConfirmationDialog(
    title: String,
    message: String,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmDelete()
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteConfirmationDialogPreview() {
    CookFriendsTheme {
        DeleteConfirmationDialog(
            title = "Delete Ingredient",
            message = "Are you sure you want to delete this ingredient?",
            onConfirmDelete = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DeleteConfirmationDialogPreviewDark() {
    CookFriendsTheme {
        DeleteConfirmationDialog(
            title = "Delete Ingredient",
            message = "Are you sure you want to delete this ingredient?",
            onConfirmDelete = {},
            onDismiss = {}
        )
    }
}
