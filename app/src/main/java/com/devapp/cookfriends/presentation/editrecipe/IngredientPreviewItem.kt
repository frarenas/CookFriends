package com.devapp.cookfriends.presentation.editrecipe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import com.devapp.cookfriends.ui.theme.LightBlue
import kotlin.uuid.Uuid

@Composable
fun IngredientPreviewItem(
    ingredient: Ingredient,
    onDeleteRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${ingredient.name}: ${ingredient.quantity} ${ingredient.measurement}",
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onDeleteRequest) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_ingredient),
                    tint = LightBlue
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientPreviewItemPreview() {
    CookFriendsTheme {
        IngredientPreviewItem(
            ingredient = Ingredient(
                name = "Ingredient",
                quantity = "1",
                measurement = "kg",
                recipeId = Uuid.random()
            ),
            onDeleteRequest = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun IngredientPreviewItemPreviewDark() {
    CookFriendsTheme {
        IngredientPreviewItem(
            ingredient = Ingredient(
                name = "Ingredient",
                quantity = "1",
                measurement = "kg",
                recipeId = Uuid.random()
            ),
            onDeleteRequest = {}
        )
    }
}
