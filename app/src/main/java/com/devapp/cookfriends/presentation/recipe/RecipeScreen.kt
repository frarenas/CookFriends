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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.devapp.cookfriends.ui.theme.LightBlue
import com.devapp.cookfriends.ui.theme.Red
import com.devapp.cookfriends.util.toShortFormat
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    navigateToIngredientCalculator: (recipeId: Uuid) -> Unit,
    navigateToEditRecipe: (recipeId: Uuid) -> Unit,
    navigateBack: () -> Unit,
    viewModel: RecipeViewModel = hiltViewModel()
) {

    val recipeState by viewModel.recipeState.collectAsState()
    val isUserLogged by viewModel.isUserLogged.collectAsState()
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
                },
                actions = if (isUserLogged) {
                    {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (recipeState.recipe?.isUserFavorite == true) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = stringResource(R.string.favorite),
                                tint = Red
                            )
                        }
                    }
                } else {
                    {}
                }
            )
        },
        floatingActionButton = if (recipeState.isEditable) {
            {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { navigateToEditRecipe(recipeState.recipe!!.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_recipe)
                    )
                }
            }
        } else {
            {}
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
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
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
                            recipe.user?.username!!,
                            recipe.date.toShortFormat()
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = recipe.description ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Ingredients
                    Text(
                        text = stringResource(R.string.ingredients),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(
                                    R.string.portions_format,
                                    recipe.portions ?: 0
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )

                            if (recipe.ingredients.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    recipe.ingredients.forEach { ingredient ->
                                        IngredientPreviewItem(
                                            ingredient = ingredient,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                        IconButton(onClick = {
                            navigateToIngredientCalculator(recipe.id)
                        }) {
                            Icon(
                                Icons.Default.Calculate,
                                contentDescription = stringResource(R.string.calculate_ingredients),
                                tint = LightBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Steps
                    Text(
                        text = stringResource(R.string.preparation),
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
                    Spacer(modifier = Modifier.height(16.dp))
                    // Comments
                    Text(
                        text = stringResource(R.string.comments),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}
