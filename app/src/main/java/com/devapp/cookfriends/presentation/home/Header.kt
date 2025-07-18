package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.SearchOptions
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import com.devapp.cookfriends.ui.theme.White

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String = "",
    showSearchBar: Boolean = true,
    initialOptions: SearchOptions? = null,
    onSearchClick: (SearchOptions) -> Unit,
    onSearchOptionsClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var currentSearchText by remember { mutableStateOf(initialOptions?.searchText ?: "") }
    LaunchedEffect(initialOptions?.searchText) {
        val newSearchText = initialOptions?.searchText ?: ""
        if (currentSearchText != newSearchText) {
            currentSearchText = newSearchText
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        White,
                        MaterialTheme.colorScheme.primary
                    )
                )
            )
            .padding(16.dp)
    ) {
        if (showSearchBar) {
            OutlinedTextField(
                value = currentSearchText,
                onValueChange = { currentSearchText = it },
                modifier = modifier.fillMaxWidth(),
                leadingIcon = {
                    IconButton(onClick = onSearchOptionsClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.more_options)
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        onSearchClick(
                            (initialOptions ?: SearchOptions()).copy(searchText = currentSearchText)
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                },
                label = { Text(stringResource(R.string.search_recipes)) },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClick(
                            (initialOptions ?: SearchOptions()).copy(searchText = currentSearchText)
                        )
                        focusManager.clearFocus() // Hide keyboard
                    }
                )
            )
        }
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = title,
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    CookFriendsTheme {
        Header(Modifier, "Recipes", initialOptions = SearchOptions(), onSearchClick = {}, onSearchOptionsClick = {})
    }
}
