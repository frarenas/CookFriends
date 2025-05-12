package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.R

@Composable
fun RecipesScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val homeState by viewModel.homeState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()) {
        Header(
            modifier = Modifier,
            title = stringResource(R.string.recipes)
        )
        Content(homeState)
    }
}
