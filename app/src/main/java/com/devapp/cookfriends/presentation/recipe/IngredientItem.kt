package com.devapp.cookfriends.presentation.recipe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import kotlin.uuid.Uuid

@Composable
fun IngredientItem(
    ingredient: Ingredient,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${ingredient.name}: ${ingredient.quantity} ${ingredient.measurement}",
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientPreviewItemPreview() {
    CookFriendsTheme {
        IngredientItem(
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
        IngredientItem(
            ingredient = Ingredient(
                name = "Ingredient",
                quantity = "1",
                measurement = "kg",
                recipeId = Uuid.random()
            )
        )
    }
}
