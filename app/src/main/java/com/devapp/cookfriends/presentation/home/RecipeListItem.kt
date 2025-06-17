package com.devapp.cookfriends.presentation.home

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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.ui.theme.Gold
import com.devapp.cookfriends.ui.theme.Red

@Composable
fun RecipeListItem(
    recipe: Recipe,
    onFavoriteClick: (Recipe) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = recipe.photos[0].url,
            contentDescription = recipe.name!!,
            error = painterResource(R.drawable.logo),
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1F)) {
            Text(text = recipe.name!!, fontSize = 24.sp)
            Text(text = stringResource(R.string.category, recipe.type!!))
            Text(text = stringResource(R.string.author, recipe.author!!))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = { onFavoriteClick(recipe) }) {
                Icon(
                    imageVector = if (recipe.isUserFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorite),
                    tint = if (recipe.isUserFavorite) Red else Color.Gray
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
    HorizontalDivider()
}
