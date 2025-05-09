package com.devapp.cookfriends.presentation.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Login",
            modifier = modifier.fillMaxWidth().padding(4.dp)
        )
        Text(
            text = "Login4",
            modifier = modifier
        )
        Textos("ejemplo")
        Button(onClick = {}) {

            Text(
                text = "iniciar sesión"
            )
        }
    }
}

@Composable
fun Textos(text: String) {
    Text(
        text = text
    )
}