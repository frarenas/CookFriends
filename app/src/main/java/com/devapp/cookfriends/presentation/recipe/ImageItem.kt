package com.devapp.cookfriends.presentation.recipe

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.common.CFAsyncImage
import com.devapp.cookfriends.ui.theme.CookFriendsTheme

@Composable
fun ImageItem(imageUrl: String, modifier: Modifier) {
    CFAsyncImage(
        imageUrl = imageUrl,
        imageDescription = stringResource(R.string.image_preview),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ImagePreviewItemPreview() {
    CookFriendsTheme {
        ImageItem(
            imageUrl = "",
            Modifier.height(100.dp)
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ImagePreviewItemDark() {
    CookFriendsTheme {
        ImageItem(
            imageUrl = "",
            Modifier.height(100.dp)
        )
    }
}
