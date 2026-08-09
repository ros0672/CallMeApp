package com.callme.feature.auth.domain.model

// Screen state
sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data class Error(val message: String?): AuthUiState()
}

// One-time events
sealed class AuthEffect {
    data object NavigateToMain : AuthEffect()
    data class ShowError(val message: String) : AuthEffect()
}