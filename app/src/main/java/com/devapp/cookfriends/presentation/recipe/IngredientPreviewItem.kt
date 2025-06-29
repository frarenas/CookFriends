package com.devapp.cookfriends.presentation.recipe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import kotlin.uuid.Uuid

@Composable
fun IngredientPreviewItem(
    ingredient: Ingredient,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
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
            )
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
            )
        )
    }
}
