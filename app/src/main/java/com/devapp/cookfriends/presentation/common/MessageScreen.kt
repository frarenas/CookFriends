package com.devapp.cookfriends.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import com.devapp.cookfriends.ui.theme.LightGray

@Composable
fun MessageScreen(
    message: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = message,
                tint = LightGray,
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(
            text = message,
            color = LightGray,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MessageScreen(
    @StringRes message: Int,
    modifier: Modifier = Modifier,
    imageVector: ImageVector
) {
    MessageScreen(message = stringResource(message), modifier = modifier, imageVector = imageVector)
}

@Preview(showBackground = true)
@Composable
fun MessageScreenPreview() {
    CookFriendsTheme {
        MessageScreen(
            "Test"
        )
    }
}
