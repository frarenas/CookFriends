package com.devapp.cookfriends.presentation.ingredientcalculator

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.CountUserCalculatedRecipesUseCase
import com.devapp.cookfriends.domain.usecase.GetLoggedUserUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import com.devapp.cookfriends.domain.usecase.SaveRecipeUseCase
import com.devapp.cookfriends.presentation.navigation.IngredientCalculator
import com.devapp.cookfriends.presentation.navigation.UuidNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@HiltViewModel
class IngredientCalculatorViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val countUserCalculatedRecipesUseCase: CountUserCalculatedRecipesUseCase, // Injected
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: IngredientCalculator = savedStateHandle.toRoute(
        typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)
    )
    private val recipeId: Uuid? = navArgs.id

    private val _state = MutableStateFlow(IngredientCalculatorState())
    val state: StateFlow<IngredientCalculatorState> = _state

    private val _showConfirmationDialog = mutableStateOf(false)
    val showConfirmationDialog: State<Boolean> = _showConfirmationDialog

    init {
        loadRecipe()
    }

    private fun loadRecipe() {
        if (recipeId != null) {
            viewModelScope.launch {
                val recipeFlow = getRecipeUseCase(recipeId)
                recipeFlow.collect { recipe ->
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
        } else return
    }

    fun onPortionChange(newPortions: Int) {
        val currentState = _state.value
        _state.value = currentState.copy(
            desiredPortions = newPortions,
            adjustedIngredients = adjustIngredients(
                currentState.ingredients,
                currentState.originalPortions,
                newPortions
            )
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
        viewModelScope.launch {
            val loggedUser = getLoggedUserUseCase()
            val currentCalculatedCount = countUserCalculatedRecipesUseCase(loggedUser.id)

            if (currentCalculatedCount >= USER_CALCULATED_RECIPE_LIMIT) {
                _state.update {
                    it.copy(
                        message = UiMessage(
                            uiText = UiText.StringResource(R.string.user_calculated_recipe_limit_reached),
                            blocking = false
                        )
                    )
                }
                return@launch
            }

            val currentState = _state.value
            if (recipeId != null) {
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
                        user = loggedUser,
                        portions = currentState.desiredPortions,
                        ingredients = copiedIngredients,
                        steps = copiedSteps,
                        recipePhotos = copiedRecipePhotos,
                        userCalculated = true,
                        date = Clock.System.now(),
                        updatePending = false
                    )
                    try {
                        saveRecipeUseCase(copiedRecipe)
                        _showConfirmationDialog.value = true
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                message = UiMessage(
                                    uiText = UiText.StringResource(R.string.generic_error),
                                    blocking = false
                                )
                            )
                        }
                        Log.e("IngredientCalculatorViewModel", "Error saving recipe", e)
                    }
                }
            }
        }
    }

    fun onClearMessage() {
        _state.update { it.copy(message = null) }
    }

    companion object {
        const val USER_CALCULATED_RECIPE_LIMIT = 10
    }
}
