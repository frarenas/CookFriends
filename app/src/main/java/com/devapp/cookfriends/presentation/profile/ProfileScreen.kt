package com.devapp.cookfriends.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.common.ConfirmationDialog
import com.devapp.cookfriends.presentation.home.Header
import com.devapp.cookfriends.util.SecureScreenEffect

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onLogout: () -> Unit
) {
    val profileState by viewModel.profileState.collectAsState()
    val newPassword by viewModel.newPassword.collectAsState()
    val repeatPassword by viewModel.repeatPassword.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    SecureScreenEffect()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ProfileNavigationEvent.NavigateToLogin -> {
                    onLogout()
                }
            }
        }
    }
    val uiEnabled = !profileState.isChangingPassword && !profileState.isLoggingOut
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header(
            modifier = Modifier,
            title = stringResource(R.string.profile),
            showSearchBar = false,
            onSearchClick = {},
            onSearchOptionsClick = { }
        )
        Box(modifier = Modifier.fillMaxSize()) {
            LaunchedEffect(key1 = profileState.message) {
                profileState.message?.let {
                    snackbarHostState.showSnackbar(
                        message = it.uiText.asString(context),
                        duration = SnackbarDuration.Short
                    )
                    viewModel.onClearMessage()
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.change_password),
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { viewModel.onNewPasswordChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    enabled = uiEnabled,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description =
                            if (passwordVisible) stringResource(R.string.hide_password) else stringResource(
                                R.string.show_password
                            )

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = { Text(stringResource(R.string.password)) },
                    singleLine = true,
                    isError = profileState.passwordErrorMessage != null,
                    supportingText = {
                        profileState.passwordErrorMessage?.let {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.asString(context),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                OutlinedTextField(
                    value = repeatPassword,
                    onValueChange = { viewModel.onRepeatPasswordChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    enabled = uiEnabled,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description =
                            if (passwordVisible) stringResource(R.string.hide_password) else stringResource(
                                R.string.show_password
                            )

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = { Text(stringResource(R.string.repeat_password)) },
                    singleLine = true,
                    isError = profileState.repeatPasswordErrorMessage != null,
                    supportingText = {
                        profileState.repeatPasswordErrorMessage?.let {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.asString(context),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                Button(
                    onClick = { viewModel.updatePassword() },
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    enabled = uiEnabled
                ) {
                    if (profileState.isChangingPassword)
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    else
                        Text(stringResource(R.string.change_password))
                }
                HorizontalDivider()
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { showConfirmationDialog = true },
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    enabled = uiEnabled
                ) {
                    if (profileState.isLoggingOut)
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    else
                        Text(stringResource(R.string.logout))
                }
            }
        }
    }

    if (showConfirmationDialog) {
            ConfirmationDialog(
                title = stringResource(R.string.confirm_logout_title),
                message = stringResource(R.string.confirm_logout_message),
                confirmText = stringResource(R.string.logout),
                dismissText = stringResource(R.string.cancel),
                onConfirm = {
                    viewModel.logout()
                    showConfirmationDialog = false
                },
                onDismiss = {
                    showConfirmationDialog = false
                }
            )
        }
}
