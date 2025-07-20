package com.devapp.cookfriends.presentation.ingredientcalculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.presentation.common.ConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientCalculatorScreen(
    navigateBack: () -> Unit,
    viewModel: IngredientCalculatorViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val portionsState = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationDialog = viewModel.showConfirmationDialog.value
    val context = LocalContext.current

    LaunchedEffect(state.desiredPortions) {
        portionsState.value = state.desiredPortions.toString()
    }

    LaunchedEffect(key1 = state.message) {
        state.message?.let {
            if(it.blocking) {
                showConfirmationDialog = true
            } else {
                snackbarHostState.showSnackbar(
                    message = it.uiText.asString(context),
                    duration = SnackbarDuration.Short
                )
            }
        }
        viewModel.onClearMessage()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ingredient_calculator)) },
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = state.desiredPortions.toString(),
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            portionsState.value = newValue

                            val parsed = newValue.toFloatOrNull()
                            if (parsed != null) {
                                val rounded = kotlin.math.ceil(parsed).toInt()
                                viewModel.onPortionChange(rounded)
                            }
                        }
                    },
                    label = { Text(stringResource(R.string.portions)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            state.adjustedIngredients.forEach { ingredient ->
                IngredientDisplayRow(ingredient)
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    viewModel.saveAdjustedRecipeLocally()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.recipe_saved),
            message = stringResource(R.string.recipe_saved_in_my_recipes),
            confirmText = stringResource(R.string.understood),
            dismissText = null,
            onConfirm = {
                navigateBack()
            },
            onDismiss = { }
        )
    }
}

@Composable
fun IngredientDisplayRow(
    ingredient: Ingredient,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = ingredient.quantity,
            onValueChange = { },
            label = {
                Text(
                    if (ingredient.measurement.isNullOrBlank()) {
                        ingredient.name
                    } else {
                        stringResource(
                            R.string.ingredient_label_format,
                            ingredient.name,
                            ingredient.measurement!!
                        )
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = false
        )
    }
}
