package com.cook_friends.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.home.HomeScreen
import com.devapp.cookfriends.presentation.navigation.Home
import com.devapp.cookfriends.presentation.navigation.RecoveryPassword

@Composable
fun LoginScreen(
    modifier: Modifier,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 64.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // 1. Título
                Text (
                    text = "Cook Friends",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                )
                // 2. Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo de la aplicación",
                    modifier = Modifier
                        .size(270.dp)
                        .padding(bottom = 6.dp)
                )

                // 3. Campo de Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true
                )

                //Spacer(modifier = Modifier.height(16.dp))

                // 4. Campo de Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                //Spacer(modifier = Modifier.height(32.dp))

                // 5. Checkbox de Recordar Usuario
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    var checked by remember { mutableStateOf(false) }
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                    Text("Mantenerme conectado")
                }

                // 6. Botón de Login
                Button(
                    onClick = {
                        navController.navigate(Home)
                        //println("Usuario: $username, Contraseña: $password")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("Login")
                }

                // 7. Botón de Continuar como Invitado
                Button(
                    onClick = {
                    navController.navigate(Home)
                    //println("Invitado")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continuar como invitado")
                }

                // 8. Opciones alternativas
                Row (
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            navController.navigate(RecoveryPassword)
                        },
                        modifier = Modifier.wrapContentWidth()
                        ) {
                        Text (
                            text = "¿Olvidaste tu contraseña?",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    TextButton(
                        onClick = { println("Registrarse") },
                        modifier = Modifier.wrapContentWidth()
                        ) {
                        Text(
                            text = "¿No tienes una cuenta?",
                            style = MaterialTheme.typography.bodySmall
                            )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Para la preview, puedes envolverlo en un tema si lo tienes
    // TuAppTheme {
    LoginScreen(modifier = Modifier, navController = rememberNavController())
    // }
}