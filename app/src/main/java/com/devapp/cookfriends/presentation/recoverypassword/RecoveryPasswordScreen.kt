package com.devapp.cookfriends.presentation.recoverypassword

import PreviewRecoveryViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // Si usas Hilt
import androidx.lifecycle.viewmodel.compose.viewModel // Para uso sin Hilt
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devapp.cookfriends.domain.models.RecoveryStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryPasswordScreen(
    navController: NavController,
    viewModel: RecoveryPasswordViewModel = viewModel() // O hiltViewModel()
) {
    val currentStep = viewModel.currentStep.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    // Para mostrar Snackbars por errores
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearErrorMessage() // Limpiar para que no se muestre de nuevo
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentStep) {
                            RecoveryStep.EnterEmail -> "Recuperar contraseña"
                            RecoveryStep.EnterCode -> "Ingresa el Código"
                            RecoveryStep.EnterNewPassword -> "Nueva Contraseña"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Aquí podrías tener lógica más fina,
                        // como volver al paso anterior dentro del ViewModel o salir de la pantalla
                        if (currentStep == RecoveryStep.EnterEmail) {
                            navController.popBackStack()
                        } else {
                            // Lógica para volver al paso anterior si quieres, o simplemente salir
                            // viewModel.goToPreviousStep() // Necesitarías implementar esto
                            navController.popBackStack() // Opciones: salir o manejar internamente
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            when (currentStep) {
                is RecoveryStep.EnterEmail -> EnterEmailStep(
                    email = viewModel.email.value,
                    onEmailChange = viewModel::onEmailChange,
                    onSubmit = viewModel::submitEmail,
                    isLoading = isLoading
                )
                is RecoveryStep.EnterCode -> EnterCodeStep(
                    code = viewModel.code.value,
                    onCodeChange = viewModel::onCodeChange,
                    onSubmit = viewModel::submitCode,
                    isLoading = isLoading
                )
                is RecoveryStep.EnterNewPassword -> EnterNewPasswordStep(
                    password = viewModel.password.value,
                    confirmPassword = viewModel.confirmPassword.value,
                    onPasswordChange = viewModel::onPasswordChange,
                    onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                    onSubmit = viewModel::submitNewPassword,
                    isLoading = isLoading
                )
            }
        }
    }
}

@Composable
fun EnterEmailStep(
    email: String,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ingrese el mail con el que se registró a Cook Friends. Enviaremos un código para generar una nueva contraseña.", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSubmit, enabled = !isLoading, modifier = Modifier.fillMaxWidth()) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            else Text("Solicitar código")
        }
    }
}

@Composable
fun EnterCodeStep(
    code: String,
    onCodeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ingrese el código que le enviamos al mail.", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            label = { Text("Código de Verificación") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSubmit, enabled = !isLoading, modifier = Modifier.fillMaxWidth()) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            else Text("Verificar Código")
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
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ingresa tu nueva contraseña.", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirmar Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSubmit, enabled = !isLoading, modifier = Modifier.fillMaxWidth()) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            else Text("Cambiar Contraseña")
        }
    }
}


@Preview(showBackground = true, name = "Recovery Step: Enter Email")
@Composable
fun RecoveryPasswordScreenPreview_EnterEmail() {
    val navController = rememberNavController()
    MaterialTheme {
        RecoveryPasswordScreen(
            navController = navController,
            viewModel = viewModel<PreviewRecoveryPasswordViewModel>( // Especifica el tipo aquí
                factory = PreviewRecoveryViewModelFactory(RecoveryStep.EnterEmail)
            )
        )
    }
}

@Preview(showBackground = true, name = "Recovery Step: Enter Code")
@Composable
fun RecoveryPasswordScreenPreview_EnterCode() {
    val navController = rememberNavController()
    MaterialTheme {
        RecoveryPasswordScreen(
            navController = navController,
            viewModel = viewModel<PreviewRecoveryPasswordViewModel>(
                factory = PreviewRecoveryViewModelFactory(RecoveryStep.EnterCode)
            )
        )
    }
}

@Preview(showBackground = true, name = "Recovery Step: New Password")
@Composable
fun RecoveryPasswordScreenPreview_NewPassword() {
    val navController = rememberNavController()
    MaterialTheme {
        RecoveryPasswordScreen(
            navController = navController,
            viewModel = viewModel<PreviewRecoveryPasswordViewModel>( // Aquí está el cambio
                factory = PreviewRecoveryViewModelFactory(RecoveryStep.EnterNewPassword)
            )
        )
    }
}

// ViewModel de utilidad para Previews
class PreviewRecoveryPasswordViewModel(initialStep: RecoveryStep) : RecoveryPasswordViewModel() {
    init {
        // Truco para establecer el estado inicial para la preview
        // Esto es un poco un hack, idealmente el ViewModel no debería exponer su _currentStep así,
        // pero para previews es una forma sencilla.
        val currentStepField = RecoveryPasswordViewModel::class.java.getDeclaredField("_currentStep")
        currentStepField.isAccessible = true
        currentStepField.set(this, mutableStateOf(initialStep))
    }
}