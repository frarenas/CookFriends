package com.devapp.cookfriends.presentation.editrecipe

import android.util.Log
import android.webkit.URLUtil
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.RecipePhoto
import com.devapp.cookfriends.domain.model.RecipeType
import com.devapp.cookfriends.domain.model.Step
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.GetLoggedUserUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeTypesUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import com.devapp.cookfriends.domain.usecase.SaveRecipeUseCase
import com.devapp.cookfriends.domain.usecase.UploadRecipeUseCase
import com.devapp.cookfriends.presentation.navigation.EditRecipe
import com.devapp.cookfriends.presentation.navigation.UuidNavType
import com.devapp.cookfriends.util.ConnectivityObserver
import com.devapp.cookfriends.util.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlin.text.trim
import kotlin.time.Clock
import kotlin.uuid.Uuid

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getRecipeTypesUseCase: GetRecipeTypesUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val uploadRecipeUseCase: UploadRecipeUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val connectivityObserver: ConnectivityObserver,
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

    private val _newImageUrl = MutableStateFlow("")
    val newImageUrl: StateFlow<String> = _newImageUrl

    private val _newIngredient = MutableStateFlow(
        Ingredient(
            name = "",
            quantity = "",
            measurement = "",
            recipeId = _editRecipeState.value.recipe.id
        )
    )
    val newIngredient: StateFlow<Ingredient> = _newIngredient

    private val _newStep = MutableStateFlow(
        Step(
            content = "",
            order = 0,
            recipeId = _editRecipeState.value.recipe.id
        )
    )
    val newStep: StateFlow<Step> = _newStep

    init {
        getAvailableRecipeTypes()
        getRecipe()
    }

    fun getRecipe() {
        if (recipeId == null) {
            _editRecipeState.update {
                it.copy(isLoading = false, recipe = Recipe(), message = null)
            }
            return
        }
        viewModelScope.launch {
            getRecipeUseCase(recipeId)
                .onStart {
                    _editRecipeState.update { it.copy(isLoading = true, message = null) }
                }
                .catch { e ->
                    _editRecipeState.update {
                        it.copy(
                            isLoading = false,
                            message = UiMessage(
                                uiText = if (e.message != null) UiText.DynamicString(
                                    e.message ?: ""
                                ) else UiText.StringResource(R.string.generic_error),
                                blocking = false
                            )
                        )
                    }
                    Log.e("EditRecipeViewModel", "Error getting recipe", e)
                }
                .collect { recipe ->
                    if (recipe == null) {
                        _editRecipeState.update {
                            it.copy(
                                isLoading = false,
                                message = UiMessage(
                                    uiText = UiText.StringResource(R.string.recipe_not_found),
                                    blocking = true
                                )
                            )
                        }
                    } else {
                        _editRecipeState.update {
                            it.copy(
                                recipe = recipe,
                                isLoading = false,
                                //message = null
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
            val recipe = _editRecipeState.value.recipe
            val recipePhoto = RecipePhoto(
                url = _newImageUrl.value.trim(),
                recipeId = _editRecipeState.value.recipe.id
            )
            val photos = recipe.recipePhotos.toMutableList()
            photos.remove(recipePhoto)
            photos.add(recipePhoto)
            recipe.recipePhotos = photos
            _editRecipeState.update { it.copy(recipe = recipe) }

            _newImageUrl.update { "" }
        }
    }

    fun onPhotoRemove(recipePhoto: RecipePhoto) {
        val recipe = _editRecipeState.value.recipe
        val photos = recipe.recipePhotos.toMutableList()
        photos.remove(recipePhoto)
        recipe.recipePhotos = photos
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onIngredientAdd() {
        val isIngredientValid = validateIngredient()
        if (isIngredientValid) {
            val recipe = _editRecipeState.value.recipe
            _newIngredient.value.recipeId = recipe.id
            val ingredients = recipe.ingredients.toMutableList()
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
        val recipe = _editRecipeState.value.recipe
        val ingredients = recipe.ingredients.toMutableList()
        ingredients.remove(ingredient)
        recipe.ingredients = ingredients
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onStepAdd() {
        val isStepValid = validateStep()
        if (isStepValid) {
            val recipe = _editRecipeState.value.recipe
            _newStep.value.recipeId = recipe.id
            val steps = recipe.steps.toMutableList()
            val currentStep = steps.find { step -> step.id == _newStep.value.id }
            if (currentStep == null) {
                steps.add(_newStep.value)
            }

            recipe.steps = steps.mapIndexed { index, step -> step.copy(order = index + 1) }
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
        val recipe = _editRecipeState.value.recipe
        val steps = recipe.steps.toMutableList()
        steps.remove(step)
        recipe.steps = steps.mapIndexed { index, step -> step.copy(order = index + 1) }
        _editRecipeState.update { it.copy(recipe = recipe) }
    }

    fun onClearMessage() {
        _editRecipeState.update { it.copy(message = null) }
    }

    fun saveRecipe() {
        val isRecipeValid = validateRecipe()
        if (isRecipeValid) {
            viewModelScope.launch {
                _editRecipeState.update { it.copy(isUploadingRecipe = true, message = null) }
                try {
                    val user = getLoggedUserUseCase()
                    _editRecipeState.value.recipe.apply {
                        this.user = user
                        this.date = Clock.System.now()
                        this.updatePending = true
                        this.userCalculated = false
                    }
                    saveRecipeUseCase(_editRecipeState.value.recipe)
                    when (connectivityObserver.getCurrentNetworkStatus()) {
                        NetworkStatus.Unavailable -> {
                            _editRecipeState.update {
                                it.copy(
                                    isUploadingRecipe = false,
                                    message = UiMessage(
                                        uiText = UiText.StringResource(R.string.upload_recipe_no_internet),
                                        blocking = false
                                    )
                                )
                            }
                        }

                        NetworkStatus.Cellular -> {
                            _editRecipeState.update {
                                it.copy(
                                    isUploadingRecipe = false,
                                    message = UiMessage(
                                        uiText = UiText.StringResource(R.string.upload_recipe_data_connectivity),
                                        blocking = false,
                                        action = { uploadRecipe() },
                                        actionLabel = UiText.StringResource(R.string.upload_recipe_now)
                                    )
                                )
                            }
                        }

                        else -> {
                            uploadRecipe()
                        }
                    }
                } catch (e: Exception) {
                    _editRecipeState.update {
                        it.copy(
                            isUploadingRecipe = false,
                            message = UiMessage(
                                uiText = if (e.message != null) UiText.DynamicString(
                                    e.message ?: ""
                                ) else UiText.StringResource(R.string.generic_error),
                                blocking = false
                            )
                        )
                    }
                    Log.e("EditRecipeViewModel", "Error saving recipe", e)
                }
            }
        }
    }

    fun uploadRecipe() {
        viewModelScope.launch {
            _editRecipeState.update {
                it.copy(
                    isUploadingRecipe = true
                )
            }
            try {
                uploadRecipeUseCase(_editRecipeState.value.recipe)
                _editRecipeState.update {
                    it.copy(
                        isUploadingRecipe = false,
                        message = UiMessage(
                            uiText = UiText.StringResource(R.string.upload_recipe_success),
                            blocking = false
                        )
                    )
                }
            } catch (e: Exception) {
                _editRecipeState.update {
                    it.copy(
                        isUploadingRecipe = false,
                        message = UiMessage(
                            uiText = UiText.StringResource(R.string.upload_recipe_error),
                            blocking = false
                        )
                    )
                }
                Log.e("EditRecipeViewModel", "Error uploading recipe", e)
            }
        }
    }

    private fun validateRecipe(): Boolean {
        var isValid = true
        if (_editRecipeState.value.recipe.name.isNullOrBlank()) {
            _editRecipeState.update {
                it.copy(
                    nameErrorMessage = UiText.StringResource(R.string.recipe_name_mandatory)
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
                    descriptionErrorMessage = UiText.StringResource(R.string.recipe_description_mandatory)
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
                    recipeTypeErrorMessage = UiText.StringResource(R.string.recipe_type_mandatory)
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
                    recipePhotosErrorMessage = UiText.StringResource(R.string.photos_mandatory)
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
                    portionsErrorMessage = UiText.StringResource(R.string.portions_mandatory)
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
                    ingredientsErrorMessage = UiText.StringResource(R.string.ingredients_mandatory)
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
                    stepsErrorMessage = UiText.StringResource(R.string.steps_mandatory)
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
                    recipePhotoErrorMessage = UiText.StringResource(R.string.photo_url_invalid)
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
                    ingredientNameErrorMessage = UiText.StringResource(R.string.ingredient_name_mandatory)
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
                    ingredientQuantityErrorMessage = UiText.StringResource(R.string.ingredient_quantity_mandatory)
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
                    stepContentErrorMessage = UiText.StringResource(R.string.step_content_mandatory)
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
