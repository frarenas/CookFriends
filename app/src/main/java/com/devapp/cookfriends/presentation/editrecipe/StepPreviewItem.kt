package com.devapp.cookfriends.presentation.editrecipe

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Step
import com.devapp.cookfriends.domain.model.StepPhoto
import com.devapp.cookfriends.presentation.common.ConfirmationDialog
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import com.devapp.cookfriends.ui.theme.LightBlue
import kotlin.uuid.Uuid

@Composable
fun StepPreviewItem(
    step: Step,
    onDeleteRequest: () -> Unit,
    onAddPhoto: (step: Step) -> Unit,
    onDeletePhoto: (step: Step) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    var newImageUrl by remember { mutableStateOf("") }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var stepPhotoToDelete by remember { mutableStateOf<StepPhoto?>(null) }

    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = step.order.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(16.dp, end = 32.dp)
                        .drawBehind {
                            drawCircle(
                                color = LightBlue,
                                radius = this.size.maxDimension * 0.8F
                            )
                        }
                )
                Text(
                    text = step.content,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDeleteRequest,
                    enabled = enabled
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_ingredient),
                        tint = LightBlue
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Images
            Text(
                stringResource(R.string.images),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newImageUrl,
                    onValueChange = { newImageUrl = it },
                    label = { Text(stringResource(R.string.image_url)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    enabled = enabled
                )
                IconButton(
                    onClick = {
                        if (newImageUrl.isNotBlank()) {
                            val stepPhotos: MutableList<StepPhoto> =
                                mutableListOf()
                            stepPhotos.addAll(step.photos)
                            stepPhotos.add(
                                StepPhoto(
                                    url = newImageUrl.trim(),
                                    stepId = step.id
                                )
                            )
                            step.photos = stepPhotos
                            onAddPhoto(step)
                            newImageUrl = ""
                        }
                    },
                    enabled = enabled
                ) {
                    Icon(
                        Icons.Default.AddAPhoto,
                        contentDescription = stringResource(R.string.add_image),
                        tint = LightBlue
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (step.photos.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    step.photos.forEach { photo ->
                        ImagePreviewItem(
                            imageUrl = photo.url,
                            onDeleteRequest = {
                                stepPhotoToDelete = photo
                                showDeleteConfirmationDialog = true
                            },
                            enabled = enabled
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirmationDialog && stepPhotoToDelete != null) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_delete_title),
            message = stringResource(R.string.confirm_delete_photo_message),
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {
                val stepPhotos: MutableList<StepPhoto> = mutableListOf()
                stepPhotos.addAll(step.photos)
                stepPhotos.remove(stepPhotoToDelete)
                step.photos = stepPhotos
                onDeletePhoto(step)
                showDeleteConfirmationDialog = false
                stepPhotoToDelete = null
            },
            onDismiss = {
                showDeleteConfirmationDialog = false
                stepPhotoToDelete = null
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StepPreviewItemPreview() {
    CookFriendsTheme {
        StepPreviewItem(
            step = Step(
                content = "Machacar la palta en un bol y sazonar con sal y pimienta.",
                recipeId = Uuid.random(),
                order = 1
            ),
            onDeleteRequest = {},
            onAddPhoto = {},
            onDeletePhoto = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StepPreviewItemPreviewDark() {
    CookFriendsTheme {
        StepPreviewItem(
            step = Step(
                content = "Machacar la palta en un bol y sazonar con sal y pimienta.",
                recipeId = Uuid.random(),
                order = 1
            ),
            onDeleteRequest = {},
            onAddPhoto = {},
            onDeletePhoto = {}
        )
    }
}
