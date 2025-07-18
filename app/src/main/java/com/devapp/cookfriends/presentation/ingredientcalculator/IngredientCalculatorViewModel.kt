package com.devapp.cookfriends.presentation.ingredientcalculator

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.internal.illegalDecoyCallException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.usecase.GetLoggedUserUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import com.devapp.cookfriends.domain.usecase.SaveRecipeUseCase
import com.devapp.cookfriends.presentation.common.SnackbarMessage
import com.devapp.cookfriends.presentation.navigation.IngredientCalculator
import com.devapp.cookfriends.presentation.navigation.UuidNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@HiltViewModel
class IngredientCalculatorViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: IngredientCalculator = savedStateHandle.toRoute(
        typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)
    )
    private val recipeId: Uuid? = navArgs.id

    private val _state = MutableStateFlow(IngredientCalculatorState())
    val state: StateFlow<IngredientCalculatorState> = _state

    private val _snackbarFlow = MutableStateFlow<SnackbarMessage?>(null)
    val snackbarFlow: StateFlow<SnackbarMessage?> = _snackbarFlow

    init {
        loadRecipe()
    }

    private fun loadRecipe() {
        if (recipeId != null) {
            viewModelScope.launch {
                val recipe = getRecipeUseCase(recipeId)
                recipe.collect { recipe ->
                    recipe?.let {
                        val adjusted =
                            adjustIngredients(it.ingredients, it.portions ?: 1, it.portions ?: 1)
                        _state.value = IngredientCalculatorState(
                            ingredients = it.ingredients,
                            originalPortions = it.portions ?: 1,
                            desiredPortions = it.portions ?: 1,
                            adjustedIngredients = adjusted
                        )
                    }
                }
            }
        }
        else return
    }

    fun onPortionChange(newPortions: Int) {
        val currentState = _state.value
        _state.value = currentState.copy(
            desiredPortions = newPortions,
            adjustedIngredients = adjustIngredients(currentState.ingredients, currentState.originalPortions, newPortions)
        )
    }

    private fun adjustIngredients(
        ingredients: List<Ingredient>,
        original: Int,
        desired: Int
    ): List<Ingredient> {
        return ingredients.map { ingredient ->
            val quantityFloat = ingredient.quantity.toFloatOrNull()
            if (quantityFloat != null) {
                val scaled = quantityFloat * desired / original
                ingredient.copy(quantity = "%.2f".format(scaled))
            } else {
                // Para mostrar los valores de ingredientes que no son numericos
                ingredient
            }
        }
    }

    fun saveAdjustedRecipeLocally() {
        if (recipeId != null) {
            viewModelScope.launch {
                val currentState = _state.value
                val originalRecipe = getRecipeUseCase(recipeId).firstOrNull()
                    originalRecipe?.let { original ->
                        // Nuevo id
                        val newRecipeId = Uuid.random()
                        // Ingredientes ajustados
                        val copiedIngredients = currentState.adjustedIngredients.map {
                            it.copy(
                                id = Uuid.random(),
                                recipeId = newRecipeId
                            )
                        }
                        // Cambia id de pasos y fotos
                        val copiedSteps = original.steps.map { step ->
                            val newStepId = Uuid.random()
                            step.copy(
                                id = newStepId,
                                recipeId = newRecipeId,
                                photos = step.photos.map { photo ->
                                    photo.copy(id = Uuid.random(), stepId = newStepId)
                                }
                            )
                        }

                        val copiedRecipePhotos = original.recipePhotos.map {
                            it.copy(id = Uuid.random(), recipeId = newRecipeId)
                        }

                        // Se copia lo demas y se guarda como estaba, pero con el id nuevo
                        val copiedRecipe = original.copy(
                            id = newRecipeId,
                            user = getLoggedUserUseCase(),
                            portions = currentState.desiredPortions,
                            ingredients = copiedIngredients,
                            steps = copiedSteps,
                            recipePhotos = copiedRecipePhotos,
                        )
                        try {
                            saveRecipeUseCase(copiedRecipe)
                            _snackbarFlow.emit(SnackbarMessage.Success("Se guardaron los cambios."))
                        } catch (e: Exception) {
                            _snackbarFlow.emit(SnackbarMessage.Error("Error al guardar la receta"))
                        }
                    }
            }
        }
    }
}