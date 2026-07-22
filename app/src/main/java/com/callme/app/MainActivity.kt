package com.callme.app

import com.callme.app.ui.LocalAppComponent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.callme.app.di.ApplicationComponent
import com.callme.app.navigation.AppNavHost
import com.callme.app.ui.theme.CallMeAppTheme

class MainActivity : ComponentActivity() {
    private val applicationComponent: ApplicationComponent by lazy {
        (application as CallMeApp).applicationComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalAppComponent provides applicationComponent) {
                CallMeAppTheme {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CallMeAppTheme {
        Greeting("Android")
    }
}