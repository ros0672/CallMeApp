package com.callme.feature.splash.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.callme.feature.splash.domain.model.SplashEffect
import com.callme.ui.components.LoadingScreen

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: SplashViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SplashEffect.Authorized -> onNavigateToMain()
                SplashEffect.Unauthorized -> onNavigateToLogin()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkSession()
    }

    LoadingScreen()
}