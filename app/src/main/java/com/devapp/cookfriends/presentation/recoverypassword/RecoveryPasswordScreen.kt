package com.devapp.cookfriends.presentation.recoverypassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.domain.models.RecoveryStep
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.devapp.cookfriends.R

@Preview
@Composable
fun RecoveryPasswordScreen(viewModel: RecoveryPasswordViewModel = hiltViewModel()) {
    when (val step = viewModel.currentStep.value) {
        is RecoveryStep.EnterEmail -> EnterEmailScreen(
            email = viewModel.email.value,
            onEmailChange = { viewModel.email.value = it },
            onNext = { viewModel.nextStep() }
        )
        is RecoveryStep.EnterCode -> EnterCodeScreen(
            code = viewModel.code.value,
            onCodeChange = { viewModel.code.value = it },
            onNext = { viewModel.nextStep() }
        )
        is RecoveryStep.EnterNewPassword -> NewPasswordScreen(
            password = viewModel.password.value,
            onPasswordChange = { viewModel.password.value = it },
            onSubmit = { viewModel.submitNewPassword() }
        )
    }
}

@Composable
fun EnterEmailScreen(
    onNext: () -> Unit,
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        RecoveryPasswordHeader(
            description = "Ingrese el mail con el que se registró a Cook Friends. Enviaremos un código para generar una nueva contraseña."
        )
        //Text("Recuperar Contraseña", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Siguiente")
        }
    }
}

@Composable
fun EnterCodeScreen(
    onNext: () -> Unit,
    code: String,
    onCodeChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        RecoveryPasswordHeader(
            description = "Ingrese el código que le enviamos al mail."
        )
        //Text("Ingresa el código", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            label = { Text("Código") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Siguiente")
        }
    }
}

@Composable
fun NewPasswordScreen(
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        RecoveryPasswordHeader(
            description = "Ingrese una nueva contraseña."
        )
        //Text("Nueva contraseña", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Repetir contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSubmit,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Confirmar")
        }
    }
}

@Composable
fun RecoveryPasswordHeader(
    title: String = stringResource(R.string.recovery_password),
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

