package com.callme.app.di

import android.content.Context
import com.callme.data.di.AuthDataModule
import com.callme.di.scopes.ApplicationScope
import com.callme.feature.auth.di.AuthDependencies
import com.callme.feature.splash.di.SplashDependencies
import com.callme.network.di.NetworkModule
import com.callme.security.di.SecurityBindsModule
import com.callme.security.di.SecurityProvidesModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [
    AuthDataModule::class,
    NetworkModule::class,
    SecurityBindsModule::class,
    SecurityProvidesModule::class
])
interface ApplicationComponent: AuthDependencies, SplashDependencies {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}