package com.callme.feature.splash.domain.model

sealed interface SplashEffect {
    object Authorized : SplashEffect
    object Unauthorized : SplashEffect
}