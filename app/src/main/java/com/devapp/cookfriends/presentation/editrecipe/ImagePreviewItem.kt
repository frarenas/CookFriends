package com.devapp.cookfriends.presentation.editrecipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.common.CFAsyncImage

@Composable
fun ImagePreviewItem(imageUrl: String, onDeleteRequest: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CFAsyncImage(
                imageUrl = imageUrl,
                imageDescription = stringResource(R.string.image_preview),
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(modifier = Modifier.fillMaxWidth(), onClick = onDeleteRequest) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_image))
            }
        }
    }
}
