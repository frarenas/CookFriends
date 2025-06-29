package com.devapp.cookfriends.presentation.recipe

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.common.CFAsyncImage
import com.devapp.cookfriends.ui.theme.CookFriendsTheme

@Composable
fun ImagePreviewItem(imageUrl: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImagePreviewItemPreview() {
    CookFriendsTheme {
        ImagePreviewItem(
            imageUrl = ""
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ImagePreviewItemDark() {
    CookFriendsTheme {
        ImagePreviewItem(
            imageUrl = ""
        )
    }
}
