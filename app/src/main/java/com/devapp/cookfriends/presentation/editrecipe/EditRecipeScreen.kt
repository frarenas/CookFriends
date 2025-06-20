package com.devapp.cookfriends.presentation.editrecipe

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.model.RecipePhoto
import com.devapp.cookfriends.presentation.common.MessageScreen
import com.devapp.cookfriends.presentation.common.RecipeTypeDropDownMenu
import com.devapp.cookfriends.ui.theme.LightBlue
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    recipeId: Uuid? = null,
    mainNavController: NavHostController,
    viewModel: EditRecipeViewModel = hiltViewModel()
) {

    val editRecipeState by viewModel.editRecipeState.collectAsState()
    val availableRecipeTypes by viewModel.availableRecipeTypes.collectAsState()
    var newImageUrl by remember { mutableStateOf("") }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var recipePhotoToDelete by remember { mutableStateOf<RecipePhoto?>(null) }
    var newIngredient by remember {
        mutableStateOf<Ingredient>(
            Ingredient(
                name = "",
                quantity = "",
                measurement = "",
                recipeId = editRecipeState.recipe.id
            )
        )
    }
    var ingredientToDelete by remember { mutableStateOf<Ingredient?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (recipeId == null) stringResource(R.string.new_recipe) else
                            stringResource(R.string.edit_recipe)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        mainNavController.popBackStack()
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
            if (editRecipeState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (editRecipeState.error != null) {
                MessageScreen(
                    message = editRecipeState.error!!,
                    imageVector = Icons.Default.Error
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(R.string.recipe_information),
                        style = MaterialTheme.typography.titleLarge
                    )
                    OutlinedTextField(
                        value = editRecipeState.recipe.name ?: "",
                        onValueChange = { viewModel.onRecipeChange(editRecipeState.recipe.copy(name = it)) },
                        label = { Text(stringResource(R.string.name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = editRecipeState.recipe.description ?: "",
                        onValueChange = {
                            viewModel.onRecipeChange(
                                editRecipeState.recipe.copy(
                                    description = it
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.description)) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    RecipeTypeDropDownMenu(
                        selectedRecipeType = editRecipeState.recipe.recipeType,
                        availableRecipeTypes = availableRecipeTypes,
                        onSelectItem = { recipeType ->
                            viewModel.onRecipeChange(editRecipeState.recipe.copy(recipeType = recipeType))
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Images
                    Text(
                        stringResource(R.string.images),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newImageUrl,
                            onValueChange = { newImageUrl = it },
                            label = { Text(stringResource(R.string.image_url)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        IconButton(onClick = {
                            if (newImageUrl.isNotBlank()) {
                                val recipePhotos: MutableList<RecipePhoto> =
                                    mutableListOf<RecipePhoto>()
                                recipePhotos.addAll(editRecipeState.recipe.recipePhotos)
                                recipePhotos.add(
                                    RecipePhoto(
                                        url = newImageUrl.trim(),
                                        recipeId = editRecipeState.recipe.id
                                    )
                                )
                                viewModel.onRecipeChange(editRecipeState.recipe.copy(recipePhotos = recipePhotos))
                                newImageUrl = ""
                            }
                        }) {
                            Icon(
                                Icons.Default.AddAPhoto,
                                contentDescription = stringResource(R.string.add_image),
                                tint = LightBlue
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (editRecipeState.recipe.recipePhotos.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            editRecipeState.recipe.recipePhotos.forEachIndexed { index, photo ->
                                ImagePreviewItem(
                                    imageUrl = photo.url,
                                    onDeleteRequest = {
                                        recipePhotoToDelete = photo
                                        showDeleteConfirmationDialog = true
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Ingredients
                    Text(
                        text = stringResource(R.string.ingredients),
                        style = MaterialTheme.typography.titleLarge
                    )
                    OutlinedTextField(
                        value = editRecipeState.recipe.portions.toString(),
                        onValueChange = {
                            viewModel.onRecipeChange(
                                editRecipeState.recipe.copy(
                                    portions = it.toInt()
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.portions)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newIngredient.name,
                            onValueChange = { newIngredient = newIngredient.copy(name = it) },
                            label = { Text(stringResource(R.string.ingredient)) },
                            modifier = Modifier.weight(0.35f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = newIngredient.quantity,
                            onValueChange = { newIngredient = newIngredient.copy(quantity = it) },
                            label = { Text(stringResource(R.string.quantity)) },
                            modifier = Modifier.weight(0.35f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = newIngredient.measurement ?: "",
                            onValueChange = {
                                newIngredient = newIngredient.copy(measurement = it)
                            },
                            label = { Text(stringResource(R.string.measurement)) },
                            modifier = Modifier.weight(0.3f),
                            singleLine = true
                        )
                        IconButton(onClick = {
                            if (newIngredient.name.isNotBlank()) {
                                val ingredients: MutableList<Ingredient> =
                                    mutableListOf<Ingredient>()
                                ingredients.addAll(editRecipeState.recipe.ingredients)
                                ingredients.add(
                                    Ingredient(
                                        name = newIngredient.name,
                                        quantity = newIngredient.quantity,
                                        measurement = newIngredient.measurement,
                                        recipeId = editRecipeState.recipe.id
                                    )
                                )
                                viewModel.onRecipeChange(editRecipeState.recipe.copy(ingredients = ingredients))
                                newIngredient = Ingredient(
                                    name = "",
                                    quantity = "",
                                    measurement = "",
                                    recipeId = editRecipeState.recipe.id
                                )
                            }
                        }) {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = stringResource(R.string.add_ingredient),
                                tint = LightBlue
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (editRecipeState.recipe.ingredients.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            editRecipeState.recipe.ingredients.forEachIndexed { index, ingredient ->
                                IngredientPreviewItem(
                                    ingredient = ingredient,
                                    onDeleteRequest = {
                                        ingredientToDelete = ingredient
                                        showDeleteConfirmationDialog = true
                                    },
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
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            /*viewModel.saveRecipe(
                                editRecipeState.recipe.copy(
                                    name = recipeName,
                                    description = recipeDescription
                                )
                            )*/
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }

    if (showDeleteConfirmationDialog) {
        if (recipePhotoToDelete != null) {
            DeleteConfirmationDialog(
                title = stringResource(R.string.confirm_delete_title),
                message = stringResource(R.string.confirm_delete_photo_message),
                onConfirmDelete = {
                    val recipePhotos: MutableList<RecipePhoto> = mutableListOf<RecipePhoto>()
                    recipePhotos.addAll(editRecipeState.recipe.recipePhotos)
                    recipePhotos.remove(recipePhotoToDelete)
                    viewModel.onRecipeChange(editRecipeState.recipe.copy(recipePhotos = recipePhotos))
                    showDeleteConfirmationDialog = false
                    recipePhotoToDelete = null
                },
                onDismiss = {
                    showDeleteConfirmationDialog = false
                }
            )
        } else if (ingredientToDelete != null) {
            DeleteConfirmationDialog(
                title = stringResource(R.string.confirm_delete_title),
                message = stringResource(R.string.confirm_delete_ingredient_message),
                onConfirmDelete = {
                    val ingredients: MutableList<Ingredient> = mutableListOf<Ingredient>()
                    ingredients.addAll(editRecipeState.recipe.ingredients)
                    ingredients.remove(ingredientToDelete)
                    viewModel.onRecipeChange(editRecipeState.recipe.copy(ingredients = ingredients))
                    showDeleteConfirmationDialog = false
                    ingredientToDelete = null
                },
                onDismiss = {
                    showDeleteConfirmationDialog = false
                }
            )
        }
    }
}
