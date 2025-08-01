package com.devapp.cookfriends.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.UiMessage
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.FetchDataUseCase
import com.devapp.cookfriends.domain.usecase.IsDataFirstSyncedUseCase
import com.devapp.cookfriends.domain.usecase.IsUserLoggedUseCase
import com.devapp.cookfriends.domain.usecase.SetDataFirstSyncedUseCase
import com.devapp.cookfriends.domain.usecase.UploadPendingDataUseCase
import com.devapp.cookfriends.util.ConnectivityObserver
import com.devapp.cookfriends.util.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val isDataFirstSyncedUseCase: IsDataFirstSyncedUseCase,
    private val setDataFirstSyncedUseCase: SetDataFirstSyncedUseCase,
    private val uploadPendingDataUseCase: UploadPendingDataUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _recipesState = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState> = _recipesState

    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged: StateFlow<Boolean> = _isUserLogged

    init {
        fetchData()
        isUserLogged()
        uploadPendingData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            val dataFirstSynced = isDataFirstSyncedUseCase()
            if (connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Unavailable) {
                _recipesState.update {
                    it.copy(
                        isLoading = false,
                        message = UiMessage(
                            UiText.StringResource(R.string.no_internet_connection),
                            !dataFirstSynced
                        )
                    )
                }
            } else {
                try {
                    _recipesState.update { it.copy(isLoading = true, message = null) }
                    fetchDataUseCase()
                    setDataFirstSyncedUseCase()
                    _recipesState.update { it.copy(isLoading = false, message = null) }
                } catch (e: Exception) {
                    _recipesState.update {
                        it.copy(
                            isLoading = false,
                            message = UiMessage(
                                UiText.StringResource(R.string.generic_error),
                                !dataFirstSynced
                            )
                        )
                    }
                    Log.e("HomeViewModel", "Error fetching data", e)
                }
            }
        }
    }

    private fun uploadPendingData() {
        viewModelScope.launch {
            if (connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Wifi ||
                connectivityObserver.getCurrentNetworkStatus() == NetworkStatus.Available) {
                try {
                    uploadPendingDataUseCase()
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error uploading pending data", e)
                }
            }
        }
    }

    private fun isUserLogged() {
        viewModelScope.launch {
            _isUserLogged.value = isUserLoggedUseCase()
        }
    }

    fun onClearMessage() {
        _recipesState.update { it.copy(message = null) }
    }
}
