package com.chan.restoradar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chan.restoradar.feature.auth.LoginScreen
import com.chan.restoradar.feature.auth.RegisterScreen
import com.chan.restoradar.feature.home.HomeScreen

object Routes {
    const val LOGIN    = "login"
    const val REGISTER = "register"
    const val HOME     = "home"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestoRadarApp()
        }
    }
}

@Composable
fun RestoRadarApp() {
    val navController = rememberNavController()

    var savedFullName by remember { mutableStateOf("") }
    var savedEmail    by remember { mutableStateOf("") }
    var isNewUser     by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { _, fullName ->
                    savedFullName = fullName
                    savedEmail    = ""
                    isNewUser     = false   // came from login
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { _, fullName ->
                    savedFullName = fullName
                    savedEmail    = ""
                    isNewUser     = true    // came from register
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                fullName  = savedFullName,
                email     = savedEmail,
                isNewUser = isNewUser,
                onSignOut = {
                    savedFullName = ""
                    savedEmail    = ""
                    isNewUser     = false
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}