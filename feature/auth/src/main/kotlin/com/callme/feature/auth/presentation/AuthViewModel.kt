package com.callme.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.callme.domain.utils.Result
import com.callme.domain.utils.ValidationResult
import com.callme.feature.auth.domain.model.AuthEffect
import com.callme.feature.auth.domain.model.AuthUiState
import com.callme.feature.auth.domain.usecase.LoginUseCase
import com.callme.feature.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState.Idle)
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<AuthEffect> = MutableSharedFlow(replay = 0)
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    fun login(email: String, password: String) {
        val validation = validateLogin(email, password)
        if (validation is ValidationResult.Error) {
            _state.value = AuthUiState.Error(validation.message)
            return
        }

        viewModelScope.launch {
            _state.value = AuthUiState.Loading
            val result = loginUseCase(email, password)
            handleResult(result)
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        val validation = validateRegistration(email, password, confirmPassword)
        if (validation is ValidationResult.Error) {
            _state.value = AuthUiState.Error(validation.message)
            return
        }

        viewModelScope.launch {
            _state.value = AuthUiState.Loading
            val result = registerUseCase(email, password)
            handleResult(result)
        }
    }

    fun clearError() {
        _state.update { state ->
            if (state is AuthUiState.Error) {
                AuthUiState.Idle
            } else {
                state
            }
        }
    }

    private suspend fun handleResult(result: Result<Unit>) {
        when (result) {
            is Result.Success -> {
                _state.value = AuthUiState.Idle
                _effect.emit(AuthEffect.NavigateToMain)
            }

            is Result.Error -> {
                _state.value = AuthUiState.Idle
                _effect.emit(AuthEffect.ShowError(result.message))
            }
        }
    }

    private fun validateLogin(email: String, password: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error("Email cannot be empty")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult.Error(
                "Invalid email format"
            )

            password.length < PASSWORD_MIN_LENGTH -> ValidationResult.Error("Password must be at least $PASSWORD_MIN_LENGTH characters")
            else -> ValidationResult.Success
        }
    }

    private fun validateRegistration(
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult.Error("Email cannot be empty")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult.Error(
                "Invalid email format"
            )

            password.length < PASSWORD_MIN_LENGTH -> ValidationResult.Error("Password must be at least $PASSWORD_MIN_LENGTH characters")
            password != confirmPassword -> ValidationResult.Error("Passwords do not match")
            else -> ValidationResult.Success
        }
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }
}

class AuthViewModelFactory @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(
            loginUseCase,
            registerUseCase
        ) as T
    }
}