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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.devapp.cookfriends.presentation.common.RatingStar
import com.devapp.cookfriends.ui.theme.Gold
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
    val newComment by viewModel.newComment.collectAsState()
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
                actions = {
                    if (isUserLogged && recipeState.recipe?.userCalculated == false) {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (recipeState.recipe?.isUserFavorite == true)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Outlined.FavoriteBorder,
                                contentDescription = stringResource(R.string.favorite),
                                tint = Red
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (recipeState.isEditable && recipeState.recipe?.userCalculated == false) {
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
                        viewModel.onClearMessage()
                    }
                }
                val recipe = recipeState.recipe!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (recipe.recipePhotos.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            recipe.recipePhotos.forEach { photo ->
                                ImageItem(
                                    imageUrl = photo.url,
                                    modifier = Modifier.height(200.dp)
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
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(R.string.rating),
                            tint = Gold
                        )
                        Text(text = recipe.averageRating?.toString() ?: "-")
                        if (isUserLogged && recipeState.recipe?.userCalculated == false) {
                            Spacer(modifier = Modifier.width(16.dp))
                            RatingStar(
                                rating = recipe.userRating?.toFloat() ?: 0F,
                                maxRating = 5,
                                onStarClick = { viewModel.rateRecipe(it) },
                                isIndicator = recipeState.isSendingRating
                            )
                        }
                    }
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                        IngredientItem(
                                            ingredient = ingredient,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                        if (isUserLogged && recipeState.recipe?.userCalculated == false) {
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
                                StepItem(
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
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isUserLogged && recipeState.recipe?.userCalculated == false) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newComment,
                                onValueChange = {
                                    viewModel.onNewCommentChange(it)
                                },
                                label = { Text(stringResource(R.string.write_a_comment)) },
                                modifier = Modifier.weight(1f),
                                maxLines = 4,
                                isError = recipeState.commentErrorMessage != null,
                                supportingText = {
                                    recipeState.commentErrorMessage?.let {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = it.asString(context),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                enabled = !recipeState.isSendingComment
                            )
                            IconButton(
                                onClick = {
                                    viewModel.sendComment()
                                },
                                enabled = !recipeState.isSendingComment
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = stringResource(R.string.send),
                                    tint = LightBlue
                                )
                            }
                        }
                    }
                    if (recipe.comments.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            recipe.comments.forEach { comment ->
                                CommentItem(
                                    comment = comment,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.no_comments),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
