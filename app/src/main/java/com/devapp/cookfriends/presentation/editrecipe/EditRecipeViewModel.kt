package com.devapp.cookfriends.presentation.editrecipe

import android.webkit.URLUtil
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
import com.devapp.cookfriends.domain.usecase.GetLoggedUserUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeTypesUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import com.devapp.cookfriends.domain.usecase.SaveRecipeUseCase
import com.devapp.cookfriends.presentation.common.SnackbarMessage
import com.devapp.cookfriends.presentation.navigation.EditRecipe
import com.devapp.cookfriends.presentation.navigation.UuidNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlin.text.trim
import kotlin.uuid.Uuid

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getRecipeTypesUseCase: GetRecipeTypesUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
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

    private val _newImageUrl = MutableStateFlow<String>("")
    val newImageUrl: StateFlow<String> = _newImageUrl

    private val _newIngredient = MutableStateFlow<Ingredient>(Ingredient(
        name = "",
        quantity = "",
        measurement = "",
        recipeId = _editRecipeState.value.recipe.id
    ))
    val newIngredient: StateFlow<Ingredient> = _newIngredient

    private val _newStep = MutableStateFlow<Step>(
        Step(
            content = "",
            order = 0,
            recipeId = _editRecipeState.value.recipe.id
        )
    )
    val newStep: StateFlow<Step> = _newStep

    private val _snackbarFlow = MutableSharedFlow<SnackbarMessage>()
    val snackbarFlow: SharedFlow<SnackbarMessage> = _snackbarFlow

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
            getRecipeUseCase(recipeId)
                .onStart {
                    _editRecipeState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _editRecipeState.update {
                        it.copy(
                            isLoading = false,
                            error = e.localizedMessage ?: "An error occurred"
                        )
                    }
                }
                .collect { recipe ->
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

    fun onPortionsChange(portions: String) {
        var newPortions =
            if (_editRecipeState.value.recipe.portions != null) _editRecipeState.value.recipe.portions else 0
        try {
            newPortions = if (portions.isBlank()) {
                null
            } else {
                portions.toInt()
            }
        } catch (_: Exception) {

        } finally {
            _editRecipeState.update {
                it.copy(
                    editRecipeState.value.recipe.copy(
                        portions = newPortions
                    )
                )
            }
        }
    }

    fun onNewImageChange(imageUrl: String) {
        _newImageUrl.update { imageUrl }
    }

    fun onNewIngredientChange(ingredient: Ingredient) {
        _newIngredient.update { ingredient }
    }

    fun onNewStepChange(step: Step) {
        _newStep.update { step }
    }

    fun onPhotoAdd() {
        val isPhotoValid = validatePhotoUrl()
        if (isPhotoValid) {
            var recipe = _editRecipeState.value.recipe
            val recipePhoto = RecipePhoto(
                url = _newImageUrl.value.trim(),
                recipeId = _editRecipeState.value.recipe.id
            )
            val photos: MutableList<RecipePhoto> = mutableListOf<RecipePhoto>()
            photos.addAll(recipe.recipePhotos)
            photos.remove(recipePhoto)
            photos.add(recipePhoto)
            recipe.recipePhotos = photos
            _editRecipeState.update { it.copy(recipe = recipe) }

            _newImageUrl.update { "" }
        }
    }

    fun onPhotoRemove(recipePhoto: RecipePhoto) {
        var recipe = _editRecipeState.value.recipe
        val photos: MutableList<RecipePhoto> = mutableListOf<RecipePhoto>()
        photos.addAll(recipe.recipePhotos)
        photos.remove(recipePhoto)
        recipe.recipePhotos = photos
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onIngredientAdd() {
        val isIngredientValid = validateIngredient()
        if (isIngredientValid) {
            var recipe = _editRecipeState.value.recipe
            _newIngredient.value.recipeId = recipe.id
            val ingredients: MutableList<Ingredient> = mutableListOf<Ingredient>()
            ingredients.addAll(recipe.ingredients)
            ingredients.remove(_newIngredient.value)
            ingredients.add(_newIngredient.value)
            recipe.ingredients = ingredients
            _editRecipeState.update { it.copy(recipe = recipe) }

            _newIngredient.update {
                Ingredient(
                    name = "",
                    quantity = "",
                    measurement = "",
                    recipeId = recipe.id
                )
            }
        }
    }

    fun onIngredientRemove(ingredient: Ingredient) {
        var recipe = _editRecipeState.value.recipe
        val ingredients: MutableList<Ingredient> = mutableListOf<Ingredient>()
        ingredients.addAll(recipe.ingredients)
        ingredients.remove(ingredient)
        recipe.ingredients = ingredients
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onStepAdd() {
        val isStepValid = validateStep()
        if (isStepValid) {
            var recipe = _editRecipeState.value.recipe
            newStep.value.recipeId = recipe.id
            newStep.value.order = recipe.steps.size
            val steps: MutableList<Step> = mutableListOf<Step>()
            steps.addAll(recipe.steps)
            steps.remove(_newStep.value)
            steps.add(_newStep.value)
            recipe.steps = steps
            _editRecipeState.update { it.copy(recipe = recipe) }

            _newStep.update {
                Step(
                    content = "",
                    order = 0,
                    recipeId = recipe.id
                )
            }
        }
    }

    fun onStepRemove(step: Step) {
        var recipe = _editRecipeState.value.recipe
        val steps: MutableList<Step> = mutableListOf<Step>()
        steps.addAll(recipe.steps)
        steps.remove(step)
        recipe.steps = steps
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun saveRecipe() {
        val isRecipeValid = validateRecipe()
        if (isRecipeValid) {
            viewModelScope.launch {
                _editRecipeState.update { it.copy(isLoading = true, error = null) }
                try {
                    val user = getLoggedUserUseCase()
                    _editRecipeState.value.recipe.apply {
                        this.user = user
                        this.date = Clock.System.now()
                        this.updatePending = true
                    }
                    saveRecipeUseCase(_editRecipeState.value.recipe)
                    _editRecipeState.update { it.copy(isLoading = false, error = null) }
                    _snackbarFlow.emit(SnackbarMessage.Error("Se guardaron los cambios."))
                } catch (e: Exception) {
                    _editRecipeState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _snackbarFlow.emit(SnackbarMessage.Error(e.message ?: "Se produjo un error."))
                }
            }
        }
    }

    private fun validateRecipe(): Boolean {
        var isValid = true
        if (_editRecipeState.value.recipe.name.isNullOrBlank()) {
            _editRecipeState.update {
                it.copy(
                    nameErrorMessage = "Ingrese el nombre de la receta."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    nameErrorMessage = null
                )
            }
        }
        if (_editRecipeState.value.recipe.description.isNullOrBlank()) {
            _editRecipeState.update {
                it.copy(
                    descriptionErrorMessage = "Ingrese la descripción de la receta."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    descriptionErrorMessage = null
                )
            }
        }
        if (_editRecipeState.value.recipe.recipeType == null) {
            _editRecipeState.update {
                it.copy(
                    recipeTypeErrorMessage = "Seleccione el tipo de receta."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    recipeTypeErrorMessage = null
                )
            }
        }
        if (_editRecipeState.value.recipe.recipePhotos.isEmpty()) {
            _editRecipeState.update {
                it.copy(
                    recipePhotosErrorMessage = "Ingrese al menos una foto."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    recipePhotosErrorMessage = null
                )
            }
        }
        if (_editRecipeState.value.recipe.portions == null) {
            _editRecipeState.update {
                it.copy(
                    portionsErrorMessage = "Ingrese la cantidad de porciones."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    portionsErrorMessage = null
                )
            }
        }
        if (_editRecipeState.value.recipe.ingredients.isEmpty()) {
            _editRecipeState.update {
                it.copy(
                    ingredientsErrorMessage = "Ingrese los ingredientes."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    ingredientsErrorMessage = null
                )
            }
        }
        if (_editRecipeState.value.recipe.steps.isEmpty()) {
            _editRecipeState.update {
                it.copy(
                    stepsErrorMessage = "Ingrese los pasos."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    stepsErrorMessage = null
                )
            }
        }
        return isValid
    }

    private fun validatePhotoUrl(): Boolean {
        var isValid = true
        if (!URLUtil.isValidUrl(_newImageUrl.value)) {
            _editRecipeState.update {
                it.copy(
                    recipePhotoErrorMessage = "Ingrese una URL válida."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    recipePhotoErrorMessage = null
                )
            }
        }

        return isValid
    }

    private fun validateIngredient(): Boolean {
        var isValid = true
        if (_newIngredient.value.name.isBlank()) {
            _editRecipeState.update {
                it.copy(
                    ingredientNameErrorMessage = "Ingrese el nombre."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    ingredientNameErrorMessage = null
                )
            }
        }
        if (_newIngredient.value.quantity.isBlank()) {
            _editRecipeState.update {
                it.copy(
                    ingredientQuantityErrorMessage = "Ingrese la cantidad."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    ingredientQuantityErrorMessage = null
                )
            }
        }

        return isValid
    }

    private fun validateStep(): Boolean {
        var isValid = true
        if (_newStep.value.content.isBlank()) {
            _editRecipeState.update {
                it.copy(
                    stepContentErrorMessage = "Ingrese el paso."
                )
            }
            isValid = false
        } else {
            _editRecipeState.update {
                it.copy(
                    stepContentErrorMessage = null
                )
            }
        }

        return isValid
    }
}
