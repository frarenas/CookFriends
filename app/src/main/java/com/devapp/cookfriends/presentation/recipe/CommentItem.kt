package com.devapp.cookfriends.presentation.recipe

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.domain.model.Comment
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import kotlin.uuid.Uuid

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Column(modifier = modifier.padding(8.dp)) {
            Text(
                text = comment.userId.toString(),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = comment.comment,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    CookFriendsTheme {
        CommentItem(
            comment = Comment(
                comment = "Me encantó la combinación de ingredientes.",
                userId = Uuid.random(),
                recipeId = Uuid.random()
            )
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CommentItemPreviewDark() {
    CookFriendsTheme {
        CommentItem(
            comment = Comment(
                comment = "Me encantó la combinación de ingredientes.",
                userId = Uuid.random(),
                recipeId = Uuid.random()
            )
        )
    }
}
