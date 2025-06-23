package com.devapp.cookfriends.presentation.ingredientcalculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import com.devapp.cookfriends.R
import kotlin.uuid.Uuid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientCalculatorScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onAddToMyRecipes: () -> Unit = {},
    recipeId: Uuid? = null,
    mainNavController: NavHostController,
    viewModel: IngredientCalculatorViewModel = hiltViewModel()
) {
    val basePortions = 4f
    val baseAgua = 2f
    val baseHarina = 1
    val baseSal = 1f

    //val state = viewModel.state.value
    val state by viewModel.state.collectAsState()
    // Editable campo de porciones
    val porcionesState = remember { mutableStateOf(state.desiredPortions.toString()) }
    val ingredients = state.ingredients
    val desiredPortions = state.desiredPortions
    val originalPortions = state.originalPortions


    val porcionesFloat = porcionesState.value.toFloatOrNull() ?: basePortions

    val agua = remember(porcionesFloat) {
        String.format("%.2f", baseAgua * porcionesFloat / basePortions)
    }
    val harina = remember(porcionesFloat) {
        String.format("%.2f", baseHarina * porcionesFloat / basePortions)
    }
    val sal = remember(porcionesFloat) {
        String.format("%.2f", baseSal * porcionesFloat / basePortions)
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
                onClick = onAddToMyRecipes,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Agregar a mis recetas")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Porciones: ", modifier = Modifier.padding(end = 8.dp))
//                TextField(
//                    value = porcionesState.value,
//                    onValueChange = { porcionesState.value = it },
//                    modifier = Modifier.width(60.dp),
//                    singleLine = true
//                )
                TextField(
                    value = porcionesState.value,
                    onValueChange = {
                        porcionesState.value = it
                        it.toIntOrNull()?.let { newVal -> viewModel.onPortionChange(newVal) }
                    },
                    modifier = Modifier.width(60.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

//            IngredientDisplayRow("Agua", agua, "lts")
//            IngredientDisplayRow("Harina", harina, "kg")
//            IngredientDisplayRow("Sal", sal, "crd")

//            ingredients.forEach { ingredient ->
//                val adjustedQuantity = remember(porcionesState.value) {
//                    val qty = ingredient.quantity.toFloatOrNull() ?: 0f
//                    val adjusted = qty * desiredPortions / originalPortions
//                    String.format("%.2f", adjusted)
//                }
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
            modifier = Modifier.width(60.dp),
            singleLine = true,
            enabled = false
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = if (unit != null) "$unit" else "")
    }
}
