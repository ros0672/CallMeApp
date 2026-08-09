package com.callme.feature.splash.di

import com.callme.di.scopes.FeatureScope
import com.callme.feature.splash.presentation.SplashViewModelFactory
import dagger.Component

@FeatureScope
@Component(dependencies = [SplashDependencies::class])
interface SplashComponent {
    fun splashViewModelFactory(): SplashViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(dependencies: SplashDependencies): SplashComponent
    }
}
