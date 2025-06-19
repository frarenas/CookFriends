package com.devapp.cookfriends.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.devapp.cookfriends.R

@Composable
fun CFAsyncImage(
    imageUrl: String,
    imageDescription: String,
    @DrawableRes errorImage: Int = R.drawable.logo,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = imageDescription,
        error = painterResource(errorImage),
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
    )
}
