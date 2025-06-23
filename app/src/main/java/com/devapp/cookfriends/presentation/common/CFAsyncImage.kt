package com.devapp.cookfriends.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.devapp.cookfriends.R

@Composable
fun CFAsyncImage(
    imageUrl: String,
    imageDescription: String,
    modifier: Modifier = Modifier,
    @DrawableRes errorImage: Int = R.drawable.logo
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = imageDescription,
        error = painterResource(errorImage),
        modifier = modifier
    )
}
