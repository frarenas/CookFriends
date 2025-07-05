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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.model.RecipePhoto
import com.devapp.cookfriends.domain.model.Step
import com.devapp.cookfriends.presentation.common.ConfirmationDialog
import com.devapp.cookfriends.presentation.common.MessageScreen
import com.devapp.cookfriends.presentation.common.RecipeTypeDropDownMenu
import com.devapp.cookfriends.ui.theme.LightBlue
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    recipeId: Uuid? = null,
    navigateBack: () -> Unit,
    viewModel: EditRecipeViewModel = hiltViewModel()
) {

    val editRecipeState by viewModel.editRecipeState.collectAsState()
    val availableRecipeTypes by viewModel.availableRecipeTypes.collectAsState()
    val newImageUrl by viewModel.newImageUrl.collectAsState()
    val newIngredient by viewModel.newIngredient.collectAsState()
    val newStep by viewModel.newStep.collectAsState()
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var recipePhotoToDelete by remember { mutableStateOf<RecipePhoto?>(null) }
    var ingredientToDelete by remember { mutableStateOf<Ingredient?>(null) }
    var stepToDelete by remember { mutableStateOf<Step?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var saveRecipeConfirmation by remember { mutableStateOf(false) }

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
            if (editRecipeState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (editRecipeState.message?.blocking == true) {
                MessageScreen(
                    message = editRecipeState.message!!.uiText.asString(context),
                    imageVector = Icons.Default.Error
                )
            } else {
                LaunchedEffect(key1 = editRecipeState.message) {
                    editRecipeState.message?.let {
                        val result = snackbarHostState.showSnackbar(
                            message = it.uiText.asString(context),
                            duration = SnackbarDuration.Short,
                            actionLabel = it.actionLabel?.asString(context)
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                it.action?.let { it1 -> it1() }
                                viewModel.onClearMessage()
                            }

                            SnackbarResult.Dismissed -> {
                                viewModel.onClearMessage()
                            }
                        }
                    }
                }
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
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editRecipeState.recipe.name ?: "",
                        onValueChange = { viewModel.onRecipeChange(editRecipeState.recipe.copy(name = it)) },
                        label = { Text(stringResource(R.string.name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !editRecipeState.isUploadingRecipe,
                        isError = editRecipeState.nameErrorMessage != null,
                        supportingText = {
                            editRecipeState.nameErrorMessage?.let {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = it.asString(context),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
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
                        maxLines = 4,
                        enabled = !editRecipeState.isUploadingRecipe,
                        isError = editRecipeState.descriptionErrorMessage != null,
                        supportingText = {
                            editRecipeState.descriptionErrorMessage?.let {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = it.asString(context),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                    RecipeTypeDropDownMenu(
                        selectedRecipeType = editRecipeState.recipe.recipeType,
                        availableRecipeTypes = availableRecipeTypes,
                        onSelectItem = { recipeType ->
                            viewModel.onRecipeChange(editRecipeState.recipe.copy(recipeType = recipeType))
                        },
                        errorMessage = editRecipeState.recipeTypeErrorMessage,
                        enabled = !editRecipeState.isUploadingRecipe
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
                            onValueChange = { viewModel.onNewImageChange(it) },
                            label = { Text(stringResource(R.string.image_url)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            enabled = !editRecipeState.isUploadingRecipe,
                            isError = editRecipeState.recipePhotoErrorMessage != null,
                            supportingText = {
                                editRecipeState.recipePhotoErrorMessage?.let {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = it.asString(context),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                        IconButton(
                            onClick = {
                                viewModel.onPhotoAdd()
                            },
                            enabled = !editRecipeState.isUploadingRecipe
                        ) {
                            Icon(
                                Icons.Default.AddAPhoto,
                                contentDescription = stringResource(R.string.add_image),
                                tint = LightBlue
                            )
                        }
                    }
                    editRecipeState.recipePhotosErrorMessage?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it.asString(context),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (editRecipeState.recipe.recipePhotos.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            editRecipeState.recipe.recipePhotos.forEach { photo ->
                                ImagePreviewItem(
                                    imageUrl = photo.url,
                                    onDeleteRequest = {
                                        recipePhotoToDelete = photo
                                        showConfirmationDialog = true
                                    },
                                    enabled = !editRecipeState.isUploadingRecipe
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
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = (editRecipeState.recipe.portions?.toString() ?: ""),
                        onValueChange = {
                            viewModel.onPortionsChange(it)
                        },
                        label = { Text(stringResource(R.string.portions)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        enabled = !editRecipeState.isUploadingRecipe,
                        isError = editRecipeState.portionsErrorMessage != null,
                        supportingText = {
                            editRecipeState.portionsErrorMessage?.let {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = it.asString(context),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = newIngredient.name,
                                onValueChange = {
                                    viewModel.onNewIngredientChange(newIngredient.copy(name = it))
                                },
                                label = { Text(stringResource(R.string.ingredient)) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                enabled = !editRecipeState.isUploadingRecipe,
                                isError = editRecipeState.ingredientNameErrorMessage != null,
                                supportingText = {
                                    editRecipeState.ingredientNameErrorMessage?.let {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = it.asString(context),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            )
                            Row {
                                OutlinedTextField(
                                    value = newIngredient.quantity,
                                    onValueChange = {
                                        viewModel.onNewIngredientChange(newIngredient.copy(quantity = it))
                                    },
                                    modifier = Modifier.weight(1f),
                                    label = { Text(stringResource(R.string.quantity)) },
                                    singleLine = true,
                                    enabled = !editRecipeState.isUploadingRecipe,
                                    isError = editRecipeState.ingredientQuantityErrorMessage != null,
                                    supportingText = {
                                        editRecipeState.ingredientQuantityErrorMessage?.let {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = it.asString(context),
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = newIngredient.measurement ?: "",
                                    onValueChange = {
                                        viewModel.onNewIngredientChange(
                                            newIngredient.copy(
                                                measurement = it
                                            )
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    label = { Text(stringResource(R.string.measurement)) },
                                    singleLine = true,
                                    enabled = !editRecipeState.isUploadingRecipe
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                viewModel.onIngredientAdd()
                            },

                            enabled = !editRecipeState.isUploadingRecipe
                        ) {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = stringResource(R.string.add_ingredient),
                                tint = LightBlue
                            )
                        }
                    }
                    editRecipeState.ingredientsErrorMessage?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it.asString(context),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (editRecipeState.recipe.ingredients.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            editRecipeState.recipe.ingredients.forEach { ingredient ->
                                IngredientPreviewItem(
                                    ingredient = ingredient,
                                    onDeleteRequest = {
                                        ingredientToDelete = ingredient
                                        showConfirmationDialog = true
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !editRecipeState.isUploadingRecipe
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newStep.content,
                            onValueChange = {
                                viewModel.onNewStepChange(newStep.copy(content = it))
                            },
                            modifier = Modifier.weight(1f),
                            label = { Text(stringResource(R.string.instructions)) },
                            maxLines = 4,
                            enabled = !editRecipeState.isUploadingRecipe,
                            isError = editRecipeState.stepContentErrorMessage != null,
                            supportingText = {
                                editRecipeState.stepContentErrorMessage?.let {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = it.asString(context),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                        IconButton(
                            onClick = {
                                viewModel.onStepAdd()
                            },
                            enabled = !editRecipeState.isUploadingRecipe
                        ) {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = stringResource(R.string.add_step),
                                tint = LightBlue
                            )
                        }
                    }
                    editRecipeState.stepsErrorMessage?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it.asString(context),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (editRecipeState.recipe.steps.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            editRecipeState.recipe.steps.forEach { step ->
                                StepPreviewItem(
                                    step = step,
                                    onDeleteRequest = {
                                        stepToDelete = step
                                        showConfirmationDialog = true
                                    },
                                    onAddPhoto = {
                                        viewModel.onNewStepChange(step)
                                        viewModel.onStepAdd()
                                    },
                                    onDeletePhoto = {
                                        viewModel.onNewStepChange(step)
                                        viewModel.onStepAdd()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !editRecipeState.isUploadingRecipe
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            saveRecipeConfirmation = true
                            showConfirmationDialog = true
                        },
                        enabled = !editRecipeState.isUploadingRecipe,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (editRecipeState.isUploadingRecipe)
                            CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        else
                            Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }

    if (showConfirmationDialog) {
        if (recipePhotoToDelete != null) {
            ConfirmationDialog(
                title = stringResource(R.string.confirm_delete_title),
                message = stringResource(R.string.confirm_delete_photo_message),
                confirmText = stringResource(R.string.delete),
                dismissText = stringResource(R.string.cancel),
                onConfirm = {
                    viewModel.onPhotoRemove(recipePhotoToDelete!!)
                    showConfirmationDialog = false
                    recipePhotoToDelete = null
                },
                onDismiss = {
                    showConfirmationDialog = false
                    recipePhotoToDelete = null
                }
            )
        } else if (ingredientToDelete != null) {
            ConfirmationDialog(
                title = stringResource(R.string.confirm_delete_title),
                message = stringResource(R.string.confirm_delete_ingredient_message),
                confirmText = stringResource(R.string.delete),
                dismissText = stringResource(R.string.cancel),
                onConfirm = {
                    viewModel.onIngredientRemove(ingredientToDelete!!)
                    showConfirmationDialog = false
                    ingredientToDelete = null
                },
                onDismiss = {
                    showConfirmationDialog = false
                    ingredientToDelete = null
                }
            )
        } else if (stepToDelete != null) {
            ConfirmationDialog(
                title = stringResource(R.string.confirm_delete_title),
                message = stringResource(R.string.confirm_delete_step_message),
                confirmText = stringResource(R.string.delete),
                dismissText = stringResource(R.string.cancel),
                onConfirm = {
                    viewModel.onStepRemove(stepToDelete!!)
                    showConfirmationDialog = false
                    stepToDelete = null
                },
                onDismiss = {
                    showConfirmationDialog = false
                    stepToDelete = null
                }
            )
        } else if (saveRecipeConfirmation) {
            ConfirmationDialog(
                title = stringResource(R.string.confirm_save_recipe_title),
                message = stringResource(R.string.confirm_save_recipe_message),
                confirmText = stringResource(R.string.save),
                dismissText = stringResource(R.string.cancel),
                onConfirm = {
                    viewModel.saveRecipe()
                    showConfirmationDialog = false
                    saveRecipeConfirmation = false
                },
                onDismiss = {
                    showConfirmationDialog = false
                    saveRecipeConfirmation = false
                }
            )
        }
    }
}
