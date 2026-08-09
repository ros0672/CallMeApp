package com.callme.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.callme.auth.R
import com.callme.feature.auth.domain.model.AuthEffect
import com.callme.feature.auth.domain.model.AuthUiState
import com.callme.ui.components.ActionButton
import com.callme.ui.components.LoadingScreen
import com.callme.ui.components.OutlinedTextField

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is AuthEffect.NavigateToMain -> {
                        onLoginSuccess()
                    }

                    is AuthEffect.ShowError -> {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }
        }
    }

    LoginContent(
        state = state,
        onLoginClick = viewModel::login,
        onRegisterClick = onRegisterClick,
        onClearError = viewModel::clearError,
        // TODO CallMeApp #17: implement 'forgot password' flow
        onForgotPassword = {},
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun LoginContent(
    state: AuthUiState,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onClearError: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { padding ->
        when (state) {
            is AuthUiState.Loading -> LoadingScreen()
            else -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.auth_title),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // TODO CallMeApp #18: replace with actual App Icon
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            onClearError()
                        },
                        label = stringResource(R.string.auth_login_label),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            onClearError()
                        },
                        label = stringResource(R.string.auth_password_label),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            onLoginClick(email, password)
                        }),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                val image = if (isPasswordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                }
                                val contentDescription =
                                    if (isPasswordVisible) {
                                        stringResource(R.string.auth_hide_password)
                                    } else {
                                        stringResource(R.string.auth_show_password)
                                    }

                                Icon(imageVector = image, contentDescription = contentDescription)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onForgotPassword() }) {
                            Text(
                                text = stringResource(R.string.auth_forgot_password_label),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    ActionButton(
                        text = stringResource(R.string.auth_login_label),
                        action = {
                            onClearError()
                            onLoginClick(email, password)
                        }
                    )

                    if (state is AuthUiState.Error) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message
                                ?: stringResource(R.string.auth_default_error_message),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.auth_do_not_have_account),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Light
                        )

                        TextButton(onClick = {
                            onClearError()
                            onRegisterClick()
                        }) {
                            Text(
                                text = stringResource(R.string.auth_sign_up),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun LoginContentPreview() {
    LoginContent(
        state = AuthUiState.Idle,
        onLoginClick = { _, _ -> },
        onRegisterClick = {},
        onClearError = {},
        onForgotPassword = {}
    )
}

@Composable
@Preview
fun LoginContentLoadingPreview() {
    LoginContent(
        state = AuthUiState.Loading,
        onLoginClick = { _, _ -> },
        onRegisterClick = {},
        onClearError = {},
        onForgotPassword = {}
    )
}

@Composable
@Preview
fun LoginContentErrorPreview() {
    LoginContent(
        state = AuthUiState.Error(stringResource(R.string.auth_default_error_message)),
        onLoginClick = { _, _ -> },
        onRegisterClick = {},
        onClearError = {},
        onForgotPassword = {}
    )
}