package com.devapp.cookfriends.presentation.editrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.model.RecipeType
import com.devapp.cookfriends.domain.usecase.GetRecipeTypesUseCase
import com.devapp.cookfriends.domain.usecase.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getRecipeTypesUseCase: GetRecipeTypesUseCase
) : ViewModel() {

    private val _editRecipeState = MutableStateFlow(EditRecipeState())
    val editRecipeState: StateFlow<EditRecipeState> = _editRecipeState

    private val _availableRecipeTypes = MutableStateFlow<List<RecipeType>>(emptyList())
    val availableRecipeTypes: StateFlow<List<RecipeType>> = _availableRecipeTypes.asStateFlow()

    private val _recipeId = MutableStateFlow<Uuid?>(null)
    val recipeId: StateFlow<Uuid?> = _recipeId.asStateFlow()

    init {
        getAvailableRecipeTypes()
    }

    fun getRecipe(id: Uuid) {
        viewModelScope.launch {
            _editRecipeState.update { it.copy(isLoading = true, error = null) }
            try {
                val recipe = getRecipeUseCase(id)
                _editRecipeState.update {
                    it.copy(
                        recipe = recipe,
                        isLoading = false,
                        error = null
                    )
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

    fun setRecipeId(id: Uuid) {
        _recipeId.value = id
        getRecipe(id)
    }
}
