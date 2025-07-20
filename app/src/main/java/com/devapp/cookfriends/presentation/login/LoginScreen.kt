package com.devapp.cookfriends.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devapp.cookfriends.R
import com.devapp.cookfriends.util.SecureScreenEffect
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToRecoveryPassword: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val keepMeLoggedInChecked by viewModel.keepMeLoggedInChecked.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    SecureScreenEffect()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LoginNavigationEvent.NavigateToHome -> {
                    navigateToHome()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LaunchedEffect(key1 = loginState.error) {
                loginState.error?.let {
                    snackbarHostState.showSnackbar(
                        message = it.uiText.asString(context),
                        duration = SnackbarDuration.Short
                    )
                    viewModel.onClearError()
                }
            }
            val uiEnabled = !loginState.isLoggingIn && !loginState.isLoggingInGuest
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 8.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge
                )
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .size(270.dp)
                        .padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    label = { Text(stringResource(R.string.username)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = uiEnabled,
                    isError = loginState.usernameErrorMessage != null,
                    supportingText = {
                        loginState.usernameErrorMessage?.let {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.asString(context),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text(stringResource(R.string.password)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
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
                    isError = loginState.passwordErrorMessage != null,
                    supportingText = {
                        loginState.passwordErrorMessage?.let {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.asString(context),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Checkbox(
                        checked = keepMeLoggedInChecked,
                        enabled = uiEnabled,
                        onCheckedChange = { viewModel.onKeepMeLoggedInCheckedChange(it) }
                    )
                    Text(stringResource(R.string.keep_me_logged_in))
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.login()
                    },
                    enabled = uiEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    if (loginState.isLoggingIn)
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    else
                        Text(stringResource(R.string.login))
                }

                Button(
                    onClick = {
                        viewModel.guestLogin()
                    },
                    enabled = uiEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (loginState.isLoggingInGuest)
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    else
                        Text(text = stringResource(R.string.continue_as_guest)
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            navigateToRecoveryPassword()
                        },
                        enabled = uiEnabled,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                    TextButton(
                        onClick = {
                            val signUpUrl = "https://www.example.com/signup"
                            try {
                                uriHandler.openUri(signUpUrl)
                            } catch (_: Exception) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.link_could_not_be_opened),
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        enabled = uiEnabled,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.no_account),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
