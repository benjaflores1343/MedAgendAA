package com.example.medagenda.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.medagenda.ui.screen.HomeScreen
import com.example.medagenda.ui.screen.LoginScreen
import com.example.medagenda.ui.screen.RegisterScreen
import com.example.medagenda.ui.screen.RequestAppointmentScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.definition
    ) {
        composable(
            route = Route.Login.definition,
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            exitTransition = { fadeOut(animationSpec = tween(700)) }
        ) {
            LoginScreen(
                onLoginOkNavigateHome = { userName, userRole ->
                    navController.navigate(Route.Home.build(userName, userRole)) {
                        popUpTo(Route.Login.definition) { inclusive = true }
                    }
                },
                onGoRegister = { navController.navigate(Route.Register.definition) }
            )
        }
        composable(
            route = Route.Register.definition,
            enterTransition = { 
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700))
            },
            exitTransition = { 
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700))
            }
        ) {
            RegisterScreen(
                onRegisterOkNavigateToLogin = { 
                    navController.navigate(Route.Login.definition) {
                        popUpTo(Route.Login.definition) { inclusive = true }
                    }
                 },
                onGoLogin = { navController.popBackStack() }
            )
        }
        composable(
            route = Route.Home.definition,
            arguments = listOf(
                navArgument("userName") { type = NavType.StringType },
                navArgument("userRole") { type = NavType.StringType }
            ),
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            exitTransition = { fadeOut(animationSpec = tween(700)) }
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val userRole = backStackEntry.arguments?.getString("userRole") ?: ""

            HomeScreen(
                userName = userName, 
                userRole = userRole,
                onLogout = {
                    navController.navigate(Route.Login.definition) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onGoToRequestAppointment = { navController.navigate(Route.RequestAppointment.definition) }
            )
        }
        composable(
            route = Route.RequestAppointment.definition,
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            exitTransition = { fadeOut(animationSpec = tween(700)) }
        ) {
            RequestAppointmentScreen()
        }
    }
}