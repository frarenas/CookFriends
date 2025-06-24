package com.devapp.cookfriends.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.usecase.FetchDataUseCase
import com.devapp.cookfriends.domain.usecase.IsUserLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
    private val isUserLoggedUseCase: IsUserLoggedUseCase
) : ViewModel() {

    private val _recipesState = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState> = _recipesState

    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged: StateFlow<Boolean> = _isUserLogged

    init {
        fetchData()
        isUserLogged()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _recipesState.update { it.copy(isLoading = true, error = null) }
                fetchDataUseCase()
                _recipesState.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _recipesState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "An error occurred"
                    )
                }
            }
        }
    }

    private fun isUserLogged() {
        viewModelScope.launch {
            _isUserLogged.value = isUserLoggedUseCase()
        }
    }
}
