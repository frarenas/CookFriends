package com.devapp.cookfriends.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.cookfriends.domain.usecase.ChangePasswordUseCase
import com.devapp.cookfriends.domain.usecase.LogoutUseCase
import com.devapp.cookfriends.presentation.common.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState

    private val _snackbarFlow = MutableSharedFlow<SnackbarMessage>()
    val snackbarFlow: SharedFlow<SnackbarMessage> = _snackbarFlow

    fun updatePassword(newPassword: String, repeatPassword: String) {
        viewModelScope.launch {
            _profileState.update { it.copy(isLoading = true) }
            try {
                changePasswordUseCase.execute(newPassword, repeatPassword)
                _snackbarFlow.emit(SnackbarMessage.Success("Se actulizó la contraseña."))
                _profileState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _snackbarFlow.emit(SnackbarMessage.Error(e.message ?: "Se produjo un error."))
                _profileState.update { it.copy(isLoading = true) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _profileState.update { it.copy(isLoading = true) }
                logoutUseCase()
                _profileState.update { it.copy(loggedOut = true) }
            } catch (_: Exception) {
                _profileState.update { it.copy(isLoading = false) }
            }

        }
    }
}
