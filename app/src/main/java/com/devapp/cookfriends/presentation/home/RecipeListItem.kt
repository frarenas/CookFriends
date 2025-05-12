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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.devapp.cookfriends.domain.models.Recipe
import com.devapp.cookfriends.presentation.common.RatingStar
import com.devapp.cookfriends.ui.theme.Red

@Composable
fun RecipeListItem(recipe: Recipe) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "https://www.clarin.com/2024/03/12/1jgNm90_r_340x340__1.jpg",
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = recipe.name!!, fontSize = 24.sp)
            Text(text = "Categoría: ${recipe.type}")
            Text(text = "Autor: ${recipe.author}")
        }
        Spacer(modifier = Modifier.weight(1f))
        RatingStar(recipe.rate?.toFloat() ?: 0F, maxRating = 5, {}, false)
        Spacer(modifier = Modifier.width(8.dp))
        IconButton({}) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorito", tint = Red)
        }

    }
    HorizontalDivider()
}
