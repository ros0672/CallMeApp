package com.callme.app

import android.app.Application
import com.callme.app.di.ApplicationComponent
import com.callme.app.di.DaggerApplicationComponent

class CallMeApp : Application() {
    lateinit var applicationComponent: ApplicationComponent private set

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}