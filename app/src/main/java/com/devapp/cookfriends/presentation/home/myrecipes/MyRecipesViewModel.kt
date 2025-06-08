package com.devapp.cookfriends.presentation.home.myrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.usecase.GetRecipesUseCase
import com.devapp.cookfriends.presentation.home.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _recipesState = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState> = _recipesState

    init {
        viewModelScope.launch {
            _recipesState.update { it.copy(isLoading = true, error = null) }
            getRecipesUseCase()
                .onStart {
                    _recipesState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { exception ->
                    _recipesState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.localizedMessage ?: "An error occurred"
                        )
                    }
                }
                .collect { recipes ->
                    _recipesState.update {
                        it.copy(
                            recipeList = recipes,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
}
