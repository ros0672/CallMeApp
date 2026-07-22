package com.callme.app.di

import android.content.Context
import com.callme.di.scopes.ApplicationScope
import com.callme.feature.auth.di.AuthDependencies
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [
    // modules list
])
interface ApplicationComponent: AuthDependencies {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}