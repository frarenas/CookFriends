package com.devapp.cookfriends.presentation.recipe

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.domain.model.Comment
import com.devapp.cookfriends.domain.model.User
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import com.devapp.cookfriends.ui.theme.LightGray
import com.devapp.cookfriends.util.toShortFormat
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Column(modifier = modifier.padding(8.dp)) {
            Row {
                Text(
                    text = comment.user.name ?: "",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "@${comment.user.username}",
                    style = MaterialTheme.typography.titleSmall,
                    color = LightGray
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = comment.date.toShortFormat(),
                    style = MaterialTheme.typography.titleSmall
                )
            }
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
                user = User(
                    id = Uuid.random(),
                    username = "fran",
                    name = "Francisco"
                ),
                recipeId = Uuid.random(),
                date = Clock.System.now()
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
                user = User(
                    id = Uuid.random(),
                    username = "fran",
                    name = "Francisco"
                ),
                recipeId = Uuid.random(),
                date = Clock.System.now()
            )
        )
    }
}
