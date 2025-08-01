package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.presentation.common.CFAsyncImage
import com.devapp.cookfriends.ui.theme.Gold
import com.devapp.cookfriends.ui.theme.Red
import kotlin.uuid.Uuid

@Composable
fun RecipeListItem(
    recipe: Recipe,
    isUserLogged: Boolean = false,
    onFavoriteClick: (Uuid) -> Unit,
    onDeleteClick: (Uuid) -> Unit = { },
    onItemClick: (Uuid) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(recipe.id) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CFAsyncImage(
            imageUrl = recipe.recipePhotos[0].url,
            imageDescription = recipe.name!!,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1F)) {
            Text(
                recipe.name!!,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(text = stringResource(R.string.category, recipe.recipeType?.name ?: ""))
            Text(text = stringResource(R.string.author, recipe.user?.username ?: ""))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isUserLogged) {
                if (recipe.userCalculated) {
                    IconButton(onClick = { onDeleteClick(recipe.id) }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                } else {
                    IconButton(onClick = { onFavoriteClick(recipe.id) }) {
                        Icon(
                            imageVector = if (recipe.isUserFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = stringResource(R.string.favorite),
                            tint = Red
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(R.string.rating),
                            tint = Gold
                        )
                        Text(text = recipe.averageRating?.toString() ?: "-")
                    }
                }
            }
        }
    }
    HorizontalDivider()
}
