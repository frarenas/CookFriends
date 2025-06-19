package com.devapp.cookfriends.presentation.editrecipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.devapp.cookfriends.domain.model.RecipeType
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

    var recipeName by remember(editRecipeState.recipe) {
        mutableStateOf(editRecipeState.recipe.name ?: "")
    }
    var recipeDescription by remember(editRecipeState.recipe) {
        mutableStateOf(editRecipeState.recipe.description ?: "")
    }
    var selectedRecipeType by remember { mutableStateOf<RecipeType?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    /*LaunchedEffect(editRecipeState.recipe) {
        selectedRecipeType = editRecipeState.recipe.recipeType
        if (recipeName != editRecipeState.recipe.name) {
            recipeName = editRecipeState.recipe.name ?: ""
        }
        if (recipeDescription != editRecipeState.recipe.description) {
            recipeDescription = editRecipeState.recipe.description ?: ""
        }
    }*/

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
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    OutlinedTextField(
                        value = viewModel.name.value,
                        onValueChange = viewModel::onNameChange,
                        label = { Text(stringResource(R.string.name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = viewModel.description.value,
                        onValueChange = { viewModel.onDescriptionChange(it) },
                        label = { Text(stringResource(R.string.description)) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    /*RecipeTypeDropDownMenu(
                        selectedRecipeType = viewModel.recipeType,
                        availableRecipeTypes = availableRecipeTypes
                    ) {
                            newRecipeType -> selectedRecipeType = newRecipeType
                    }*/
                    Spacer(modifier = Modifier.height(16.dp))

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
}
