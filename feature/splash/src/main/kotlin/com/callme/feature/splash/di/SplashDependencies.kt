package com.callme.feature.splash.di

import com.callme.domain.repository.AuthRepository
import com.callme.domain.storage.TokenStorage

interface SplashDependencies {
    fun authRepository(): AuthRepository
    fun tokenStorage(): TokenStorage
}