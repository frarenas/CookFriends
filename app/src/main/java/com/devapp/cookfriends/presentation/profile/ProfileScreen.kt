package com.devapp.cookfriends.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.home.Header
import com.devapp.cookfriends.ui.theme.White

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()) {
        Header(
            modifier = Modifier,
            title = stringResource(R.string.profile)
        )
        Text(stringResource(R.string.change_password))
        OutlinedTextField(
            value = password,
            onValueChange = { newText -> password = newText },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            },
            label = { stringResource(R.string.password) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White
            ),
            singleLine = true
        )
        OutlinedTextField(
            value = repeatPassword,
            onValueChange = { newText -> repeatPassword = newText },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            },
            label = { stringResource(R.string.repeat_password) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White
            ),
            singleLine = true
        )
        HorizontalDivider()
        Button(onClick = onLogout) {
            Text("Cerrar Sesión")
        }
    }
}
