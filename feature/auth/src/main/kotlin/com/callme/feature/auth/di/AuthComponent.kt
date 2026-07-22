package com.callme.feature.auth.di

import com.callme.di.scopes.FeatureScope
import com.callme.feature.auth.presentation.AuthViewModelFactory
import dagger.Component

@FeatureScope
@Component(dependencies = [AuthDependencies::class])
interface AuthComponent {
    fun authViewModelFactory(): AuthViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(dependencies: AuthDependencies): AuthComponent
    }
}