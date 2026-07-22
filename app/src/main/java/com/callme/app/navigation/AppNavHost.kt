package com.callme.app.navigation

import com.callme.app.ui.LocalAppComponent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.callme.feature.auth.di.DaggerAuthComponent
import com.callme.feature.auth.presentation.AuthViewModel
import kotlinx.serialization.Serializable
import kotlin.jvm.java

// Routes
@Serializable data object SignIn

@Serializable data object SignUp

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
     val appComponent = LocalAppComponent.current

    val authComponent = remember {
        DaggerAuthComponent.factory().create(appComponent)
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SignIn
    ) {
        composable<SignIn> {
            // TODO sign in screen with callbacks
            val viewModel = authComponent.authViewModelFactory().create(AuthViewModel::class.java)
        }

        composable<SignUp> {
            // TODO sign up screen with callbacks
            val viewModel = authComponent.authViewModelFactory().create(AuthViewModel::class.java)
        }

        // TODO main screen
    }
}