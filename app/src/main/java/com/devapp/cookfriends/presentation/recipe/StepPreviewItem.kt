package com.devapp.cookfriends.presentation.recipe

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.domain.model.Step
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import com.devapp.cookfriends.ui.theme.LightBlue
import kotlin.uuid.Uuid

@Composable
fun StepPreviewItem(
    step: Step,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = modifier.padding(end = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = step.order.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(24.dp).drawBehind {
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
            }
            if (step.photos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                ) {
                    step.photos.forEach { photo ->
                        ImagePreviewItem(
                            imageUrl = photo.url
                        )
                    }
                }
            }
        }
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
            )
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
            )
        )
    }
}
