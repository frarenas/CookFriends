package com.devapp.cookfriends.presentation.ingredientcalculator

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.internal.illegalDecoyCallException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import com.devapp.cookfriends.presentation.navigation.IngredientCalculator
import com.devapp.cookfriends.presentation.navigation.UuidNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@HiltViewModel
class IngredientCalculatorViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: IngredientCalculator = savedStateHandle.toRoute(
        typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)
    )
    private val recipeId: Uuid? = navArgs.id

    private val _state = MutableStateFlow(IngredientCalculatorState())
    val state: StateFlow<IngredientCalculatorState> = _state

    init {
        loadRecipe()
    }

    private fun loadRecipe() {
        if (recipeId != null) {
            viewModelScope.launch {
                val recipe = getRecipeUseCase(recipeId)
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
            val quantity = ingredient.quantity.toFloatOrNull() ?: 0f
            val scaled = quantity * desired / original
            ingredient.copy(quantity = "%.2f".format(scaled))
        }
    }
}