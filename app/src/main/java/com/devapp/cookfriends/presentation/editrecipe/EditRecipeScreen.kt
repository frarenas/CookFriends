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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Photo
import com.devapp.cookfriends.presentation.common.MessageScreen
import com.devapp.cookfriends.presentation.common.RecipeTypeDropDownMenu
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
    var photoToDelete by remember { mutableStateOf<Photo?>(null) }
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
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                ) {
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
                        onValueChange = { viewModel.onRecipeChange(editRecipeState.recipe.copy(description = it)) },
                        label = { Text(stringResource(R.string.description)) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    RecipeTypeDropDownMenu(
                        selectedRecipeType = editRecipeState.recipe.recipeType,
                        availableRecipeTypes = availableRecipeTypes
                    ) {
                        viewModel.onRecipeChange(editRecipeState.recipe.copy(recipeType = it))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
// --- Image URL Input Section ---
                    Text(stringResource(R.string.images), style = MaterialTheme.typography.titleMedium)
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
                                val photos: MutableList<Photo> = mutableListOf<Photo>()
                                photos.addAll(editRecipeState.recipe.photos)
                                photos.add(Photo(url = newImageUrl.trim(), recipeId = editRecipeState.recipe.id))
                                viewModel.onRecipeChange(editRecipeState.recipe.copy(photos = photos))
                                newImageUrl = ""
                            }
                        }) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = stringResource(R.string.add_image))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    if (editRecipeState.recipe.photos.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            editRecipeState.recipe.photos.forEachIndexed { index, photo ->
                                ImagePreviewItem(
                                    imageUrl = photo.url,
                                    onDeleteRequest = {
                                        /*val photos: MutableList<Photo> = mutableListOf<Photo>()
                                        photos.addAll(editRecipeState.recipe.photos)
                                        photos.remove(photo)
                                        viewModel.onRecipeChange(editRecipeState.recipe.copy(photos = photos))*/
                                        photoToDelete = photo // Set the photo to be deleted
                                        showDeleteConfirmationDialog = true
                                    }
                                )
                            }
                        }
                    }
                    // --- End Image Section ---
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

    if (showDeleteConfirmationDialog && photoToDelete != null) {
        DeleteConfirmationDialog(
            onConfirmDelete = {
                val photos: MutableList<Photo> = mutableListOf<Photo>()
                photos.addAll(editRecipeState.recipe.photos)
                photos.remove(photoToDelete)
                viewModel.onRecipeChange(editRecipeState.recipe.copy(photos = photos))
                showDeleteConfirmationDialog = false
                photoToDelete = null
            },
            onDismiss = {
                showDeleteConfirmationDialog = false
            }
        )
    }
}
