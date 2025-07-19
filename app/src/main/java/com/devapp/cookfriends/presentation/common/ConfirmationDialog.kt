package com.devapp.cookfriends.presentation.common

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.devapp.cookfriends.R
import com.devapp.cookfriends.ui.theme.CookFriendsTheme

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = dismissText?.let {{
            TextButton(
                onClick = onDismiss
            ) {
                Text(dismissText)
            }
        }}
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteConfirmationDialogPreview() {
    CookFriendsTheme {
        ConfirmationDialog(
            title = "Delete Ingredient",
            message = "Are you sure you want to delete this ingredient?",
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DeleteConfirmationDialogPreviewDark() {
    CookFriendsTheme {
        ConfirmationDialog(
            title = "Delete Ingredient",
            message = "Are you sure you want to delete this ingredient?",
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteConfirmationDialogNoDismissPreviewDark() {
    CookFriendsTheme {
        ConfirmationDialog(
            title = "Delete Ingredient",
            message = "Are you sure you want to delete this ingredient?",
            confirmText = stringResource(R.string.delete),
            dismissText = null,
            onConfirm = {},
            onDismiss = {}
        )
    }
}
