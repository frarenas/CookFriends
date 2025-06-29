package com.devapp.cookfriends.presentation.recipe

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.common.MessageScreen
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    recipeId: Uuid? = null,
    navigateBack: () -> Unit,
    viewModel: RecipeViewModel = hiltViewModel()
) {

    val recipeState by viewModel.recipeState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = recipeState.recipe?.name ?: stringResource(R.string.recipe_not_found)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (recipeState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (recipeState.message?.blocking == true) {
                MessageScreen(
                    message = recipeState.message!!.uiText.asString(context),
                    imageVector = Icons.Default.Error
                )
            } else {
                LaunchedEffect(key1 = recipeState.message) {
                    recipeState.message?.let {
                        snackbarHostState.showSnackbar(
                            message = it.uiText.asString(context),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                var recipe = recipeState.recipe!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (recipe.recipePhotos.isNotEmpty() == true) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            recipe.recipePhotos.forEach { photo ->
                                ImagePreviewItem(
                                    imageUrl = photo.url
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(
                            R.string.loaded_recipe_format,
                            recipe.user?.name!!,
                            recipe.date
                        )
                    )

                    Text(
                        text = recipe.description ?: "",
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    // Ingredients
                    Text(
                        text = stringResource(R.string.ingredients),
                        style = MaterialTheme.typography.titleLarge
                    )


                    if (recipe.ingredients.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            recipe.ingredients.forEach { ingredient ->
                                IngredientPreviewItem(
                                    ingredient = ingredient,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Steps
                    Text(
                        text = stringResource(R.string.steps),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (recipe.steps.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            recipe.steps.forEach { step ->
                                StepPreviewItem(
                                    step = step,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
