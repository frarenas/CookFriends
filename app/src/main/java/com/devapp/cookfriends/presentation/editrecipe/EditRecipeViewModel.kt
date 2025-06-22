package com.devapp.cookfriends.presentation.editrecipe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.RecipePhoto
import com.devapp.cookfriends.domain.model.RecipeType
import com.devapp.cookfriends.domain.model.Step
import com.devapp.cookfriends.domain.usecase.GetRecipeTypesUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import com.devapp.cookfriends.presentation.navigation.EditRecipe
import com.devapp.cookfriends.presentation.navigation.UuidNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getRecipeTypesUseCase: GetRecipeTypesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: EditRecipe = savedStateHandle.toRoute(
        typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)
    )
    private val recipeId: Uuid? = navArgs.id

    private val _editRecipeState = MutableStateFlow(EditRecipeState())
    val editRecipeState: StateFlow<EditRecipeState> = _editRecipeState

    private val _recipeType = mutableStateOf<RecipeType?>(null)
    val recipeType: State<RecipeType?> = _recipeType

    private val _availableRecipeTypes = MutableStateFlow<List<RecipeType>>(emptyList())
    val availableRecipeTypes: StateFlow<List<RecipeType>> = _availableRecipeTypes.asStateFlow()

    init {
        getAvailableRecipeTypes()
        getRecipe()
    }

    fun getRecipe() {
        if (recipeId == null) {
            _editRecipeState.update {
                it.copy(isLoading = false, recipe = Recipe(), error = null)
            }
            return
        }
        viewModelScope.launch {
            _editRecipeState.update { it.copy(isLoading = true, error = null) }
            try {
                val recipe = getRecipeUseCase(recipeId)
                if (recipe == null) {
                    _editRecipeState.update {
                        it.copy(
                            isLoading = false,
                            error = "Recipe not found"
                        )
                    }
                } else {
                    _editRecipeState.update {
                        it.copy(
                            recipe = recipe,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _editRecipeState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "An error occurred"
                    )
                }
            }
        }
    }

    fun getAvailableRecipeTypes() {
        viewModelScope.launch {
            getRecipeTypesUseCase()
                .onEach { recipeType ->
                    _availableRecipeTypes.value = recipeType
                }
                .launchIn(this)
        }
    }

    fun onRecipeChange(recipe: Recipe) {
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onPhotoAdd(recipePhoto: RecipePhoto) {
        var recipe = _editRecipeState.value.recipe
        val photos: MutableList<RecipePhoto> = mutableListOf<RecipePhoto>()
        photos.addAll(recipe.recipePhotos)
        photos.remove(recipePhoto)
        photos.add(recipePhoto)
        recipe.recipePhotos = photos
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onPhotoRemove(recipePhoto: RecipePhoto) {
        var recipe = _editRecipeState.value.recipe
        val photos: MutableList<RecipePhoto> = mutableListOf<RecipePhoto>()
        photos.addAll(recipe.recipePhotos)
        photos.remove(recipePhoto)
        recipe.recipePhotos = photos
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onIngredientAdd(ingredient: Ingredient) {
        var recipe = _editRecipeState.value.recipe
        val ingredients: MutableList<Ingredient> = mutableListOf<Ingredient>()
        ingredients.addAll(recipe.ingredients)
        ingredients.remove(ingredient)
        ingredients.add(ingredient)
        recipe.ingredients = ingredients
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onIngredientRemove(ingredient: Ingredient) {
        var recipe = _editRecipeState.value.recipe
        val ingredients: MutableList<Ingredient> = mutableListOf<Ingredient>()
        ingredients.addAll(recipe.ingredients)
        ingredients.remove(ingredient)
        recipe.ingredients = ingredients
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onStepAdd(step: Step) {
        var recipe = _editRecipeState.value.recipe
        val steps: MutableList<Step> = mutableListOf<Step>()
        steps.addAll(recipe.steps)
        steps.remove(step)
        steps.add(step)
        recipe.steps = steps
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onStepRemove(step: Step) {
        var recipe = _editRecipeState.value.recipe
        val steps: MutableList<Step> = mutableListOf<Step>()
        steps.addAll(recipe.steps)
        steps.remove(step)
        recipe.steps = steps
        _editRecipeState.update { it.copy(recipe = recipe) }
    }
}
