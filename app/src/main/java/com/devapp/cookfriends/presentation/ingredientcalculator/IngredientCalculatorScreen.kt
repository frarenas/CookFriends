package com.devapp.cookfriends.presentation.ingredientcalculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.devapp.cookfriends.R
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientCalculatorScreen(
    mainNavController: NavHostController,
    recipeId: Uuid? = null,
    viewModel: IngredientCalculatorViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    // Editable campo de porciones
    val porcionesState = remember { mutableStateOf("") }

    LaunchedEffect(state.desiredPortions) {
        porcionesState.value = state.desiredPortions.toString()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(viewModel.snackbarFlow) {
        viewModel.snackbarFlow.collect { message ->
            message?.let {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora de ingredientes") },
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
        bottomBar = {
            Button(
                onClick = {
                    viewModel.saveAdjustedRecipeLocally()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Agregar a mis recetas")
            }
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
                Text("Porciones: ", modifier = Modifier.padding(end = 8.dp))
                TextField(
                    value = porcionesState.value,
                    onValueChange = { newValue ->
                        // Permitir números decimales válidos (ej: 1, 2.5, 3.14)
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            porcionesState.value = newValue

                            val parsed = newValue.toFloatOrNull()
                            if (parsed != null) {
                                val rounded = kotlin.math.ceil(parsed).toInt()
                                viewModel.onPortionChange(rounded)
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier
                        .width(60.dp)
            ,
                    singleLine = true
                )


            }

            Spacer(modifier = Modifier.height(24.dp))

            state.adjustedIngredients.forEach { ingredient ->
                IngredientDisplayRow(ingredient.name, ingredient.quantity, ingredient.measurement)
            }
        }
    }
}

@Composable
fun IngredientDisplayRow(
    name: String,
    quantity: String,
    unit: String?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = "$name:", modifier = Modifier.width(80.dp))
        TextField(
            value = quantity,
            onValueChange = {}, // deshabilitado (solo lectura por ahora)
            modifier = Modifier.width(200.dp),
            singleLine = true,
            enabled = false
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = if (unit != null) "$unit" else "")
    }
}
