package com.callme.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.callme.feature.splash.domain.model.SplashEffect
import com.callme.feature.splash.domain.usecase.ValidateTokenUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val validateTokenUseCase: ValidateTokenUseCase
) : ViewModel() {
    private val _effect: MutableSharedFlow<SplashEffect> = MutableSharedFlow(replay = 0)
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

    fun checkSession() {
        viewModelScope.launch {
            val isSessionValid = validateTokenUseCase()
            _effect.emit(
                if (isSessionValid) {
                    SplashEffect.Authorized
                } else {
                    SplashEffect.Unauthorized
                }
            )
        }
    }
}

class SplashViewModelFactory @Inject constructor(
    private val validateTokenUseCase: ValidateTokenUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(validateTokenUseCase) as T
    }
}