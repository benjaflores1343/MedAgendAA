package com.example.medagenda.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.medagenda.ui.screen.HomeScreen
import com.example.medagenda.ui.screen.LoginScreen
import com.example.medagenda.ui.screen.RegisterScreen

// Objeto para centralizar las rutas y evitar errores de tipeo
object Routes {
    const val LoginScreen = "login"
    const val HomeScreen = "home"
    const val RegisterScreen = "register"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        // startDestination define la pantalla con la que arranca la app
        startDestination = Routes.LoginScreen
    ) {
        composable(
            Routes.LoginScreen,
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            exitTransition = { fadeOut(animationSpec = tween(700)) }
        ) {
            LoginScreen(
                onLoginOkNavigateHome = { 
                    navController.navigate(Routes.HomeScreen) {
                        popUpTo(Routes.LoginScreen) { inclusive = true }
                    }
                 },
                onGoRegister = { navController.navigate(Routes.RegisterScreen) }
            )
        }
        composable(
            Routes.RegisterScreen,
            enterTransition = { 
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700))
            },
            exitTransition = { 
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700))
            }
        ) {
            RegisterScreen(
                onRegisterOkNavigateToLogin = { 
                    navController.navigate(Routes.LoginScreen) {
                        popUpTo(Routes.LoginScreen) { inclusive = true }
                    }
                 },
                onGoLogin = { navController.popBackStack() }
            )
        }
        composable(
            Routes.HomeScreen,
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            exitTransition = { fadeOut(animationSpec = tween(700)) }
        ) {
            HomeScreen()
        }
    }
}