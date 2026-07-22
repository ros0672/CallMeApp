package com.callme.app.ui

import androidx.compose.runtime.compositionLocalOf
import com.callme.app.di.ApplicationComponent

val LocalAppComponent =
    compositionLocalOf<ApplicationComponent> { error("No AppComponent provided") }