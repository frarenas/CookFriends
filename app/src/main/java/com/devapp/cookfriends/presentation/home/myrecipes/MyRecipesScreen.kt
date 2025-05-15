package com.devapp.cookfriends.presentation.home.myrecipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.home.Content
import com.devapp.cookfriends.presentation.home.Header

@Composable
fun MyRecipesScreen(viewModel: MyRecipesViewModel = hiltViewModel()) {

    val recipesState by viewModel.recipesState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()) {
        Header(
            modifier = Modifier,
            title = stringResource(R.string.my_recipes)
        )
        Content(recipesState)
    }
}
