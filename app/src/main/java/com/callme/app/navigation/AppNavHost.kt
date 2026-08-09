package com.callme.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.callme.app.ui.LocalAppComponent
import com.callme.feature.auth.di.DaggerAuthComponent
import com.callme.feature.auth.presentation.LoginScreen
import com.callme.feature.auth.presentation.RegisterScreen
import com.callme.feature.splash.di.DaggerSplashComponent
import com.callme.feature.splash.presentation.SplashScreen
import kotlinx.serialization.Serializable

// Routes
@Serializable
data object Splash

@Serializable
data object SignIn

@Serializable
data object SignUp

@Serializable
data object Main

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val appComponent = LocalAppComponent.current

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Splash
    ) {
        composable<Splash> {
            val splashComponent = remember {
                DaggerSplashComponent.factory().create(appComponent)
            }
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(SignIn) {
                        popUpTo(Splash) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Main) {
                        popUpTo(Splash) { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = splashComponent.splashViewModelFactory())
            )
        }

        composable<SignIn> {
            val authComponent = remember {
                DaggerAuthComponent.factory().create(appComponent)
            }
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(SignUp)
                },
                onLoginSuccess = {
                    navController.navigate(Main) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = authComponent.authViewModelFactory())
            )
        }

        composable<SignUp> {
            val authComponent = remember {
                DaggerAuthComponent.factory().create(appComponent)
            }
            RegisterScreen(
                onLoginClick = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Main) {
                        popUpTo(id = 0) { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = authComponent.authViewModelFactory())
            )
        }

        // TODO CallMeApp #14: Implement Main screen
        composable<Main> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Main Screen")
            }
        }
    }
}