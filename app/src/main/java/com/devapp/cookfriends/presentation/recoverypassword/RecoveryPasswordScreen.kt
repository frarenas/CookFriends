package com.devapp.cookfriends.presentation.recoverypassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.RecoveryStep
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.presentation.common.ConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryPasswordScreen(
    navController: NavController,
    viewModel: RecoveryPasswordViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val passwordRecoveryState by viewModel.recoveryPasswordState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationDialog = viewModel.showConfirmationDialog.value
    val context = LocalContext.current

    LaunchedEffect(key1 = passwordRecoveryState.message) {
        passwordRecoveryState.message?.let {
            if (it.blocking) {
                showConfirmationDialog = true
            } else {
                snackbarHostState.showSnackbar(
                    message = it.uiText.asString(context),
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.onClearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (passwordRecoveryState.currentStep) {
                            RecoveryStep.EnterEmail -> stringResource(R.string.recovery_password)
                            RecoveryStep.EnterCode -> stringResource(R.string.enter_recovery_code)
                            RecoveryStep.EnterNewPassword -> stringResource(R.string.new_password)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (passwordRecoveryState.currentStep) {
                is RecoveryStep.EnterEmail -> EnterEmailStep(
                    email = viewModel.email.value,
                    onEmailChange = viewModel::onEmailChange,
                    onSubmit = viewModel::submitUsername,
                    isLoading = passwordRecoveryState.isLoading,
                    errorMessase = passwordRecoveryState.usernameErrorMessage
                )

                is RecoveryStep.EnterCode -> EnterCodeStep(
                    code = viewModel.code.value,
                    onCodeChange = viewModel::onCodeChange,
                    onSubmit = viewModel::submitCode,
                    isLoading = passwordRecoveryState.isLoading,
                    errorMessase = passwordRecoveryState.recoveryCodeErrorMessage
                )

                is RecoveryStep.EnterNewPassword -> EnterNewPasswordStep(
                    password = viewModel.newPassword.value,
                    confirmPassword = viewModel.repeatPassword.value,
                    onPasswordChange = viewModel::onPasswordChange,
                    onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                    onSubmit = viewModel::submitNewPassword,
                    isLoading = passwordRecoveryState.isLoading,
                    newPasswordErrorMessase = passwordRecoveryState.passwordErrorMessage,
                    confirmPasswordError = passwordRecoveryState.repeatPasswordErrorMessage
                )
            }
        }
    }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.password_was_updated),
            message = stringResource(R.string.log_in_with_new_password),
            confirmText = stringResource(R.string.understood),
            dismissText = null,
            onConfirm = {
                onNavigateToLogin()
            },
            onDismiss = { }
        )
    }
}

@Composable
fun EnterEmailStep(
    email: String,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean,
    errorMessase: UiText?
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.enter_user_indication),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            enabled = !isLoading,
            isError = errorMessase != null,
            supportingText = {
                errorMessase?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.asString(context),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSubmit, enabled = !isLoading, modifier = Modifier.fillMaxWidth()) {
            if (isLoading) CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            else Text(stringResource(R.string.request_code))
        }
    }
}

@Composable
fun EnterCodeStep(
    code: String,
    onCodeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean,
    errorMessase: UiText?
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.enter_code_inication),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            label = { Text(stringResource(R.string.verification_code)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            enabled = !isLoading,
            isError = errorMessase != null,
            supportingText = {
                errorMessase?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.asString(context),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSubmit, enabled = !isLoading, modifier = Modifier.fillMaxWidth()) {
            if (isLoading) CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            else Text(stringResource(R.string.verify_code))
        }
    }
}

@Composable
fun EnterNewPasswordStep(
    password: String,
    confirmPassword: String,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean,
    newPasswordErrorMessase: UiText?,
    confirmPasswordError: UiText?
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.enter_new_password_inication),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = !isLoading,
            isError = newPasswordErrorMessase != null,
            supportingText = {
                newPasswordErrorMessase?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.asString(context),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text(stringResource(R.string.repeat_password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = !isLoading,
            isError = confirmPasswordError != null,
            supportingText = {
                confirmPasswordError?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.asString(context),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSubmit, enabled = !isLoading, modifier = Modifier.fillMaxWidth()) {
            if (isLoading) CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            else Text(stringResource(R.string.change_password))
        }
    }
}
