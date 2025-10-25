package com.example.medagenda.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.medagenda.ui.screen.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.definition
    ) {
        composable(Route.Login.definition) { 
            LoginScreen(
                onLoginOkNavigateHome = { userName, userRole, pacienteId ->
                    navController.navigate(Route.Home.build(userName, userRole, pacienteId)) {
                        popUpTo(Route.Login.definition) { inclusive = true }
                    }
                },
                onGoRegister = { navController.navigate(Route.Register.definition) }
            )
        }
        composable(Route.Register.definition) {
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
                navArgument("userRole") { type = NavType.StringType },
                navArgument("pacienteId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val userRole = backStackEntry.arguments?.getString("userRole") ?: ""
            val pacienteId = backStackEntry.arguments?.getLong("pacienteId") ?: -1L

            HomeScreen(
                userName = userName, 
                userRole = userRole,
                pacienteId = pacienteId,
                onLogout = {
                    navController.navigate(Route.Login.definition) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onGoToRequestAppointment = { pId ->
                    navController.navigate(Route.RequestAppointment.build(pId))
                }
            )
        }
        composable(
            route = Route.RequestAppointment.definition,
            arguments = listOf(navArgument("pacienteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getLong("pacienteId") ?: -1L
            RequestAppointmentScreen(
                pacienteId = pacienteId,
                onSpecialtySelected = { specialtyId, pId ->
                    navController.navigate(Route.SelectDoctor.build(specialtyId, pId))
                }
            )
        }
        composable(
            route = Route.SelectDoctor.definition,
            arguments = listOf(
                navArgument("specialtyId") { type = NavType.LongType },
                navArgument("pacienteId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getLong("pacienteId") ?: -1L
            SelectDoctorScreen(
                pacienteId = pacienteId,
                onDoctorSelected = { medicoId, pId ->
                    navController.navigate(Route.SelectTimeSlot.build(medicoId, pId))
                }
            )
        }
        composable(
            route = Route.SelectTimeSlot.definition,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.LongType },
                navArgument("pacienteId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
             val medicoId = backStackEntry.arguments?.getLong("medicoId") ?: -1L
             val pacienteId = backStackEntry.arguments?.getLong("pacienteId") ?: -1L
            SelectTimeSlotScreen(
                medicoId = medicoId,
                pacienteId = pacienteId,
                onBookingConfirmed = {
                    val homeRoutePattern = Route.Home.definition.substringBefore("/")
                    navController.popBackStack(homeRoutePattern, false)
                }
            )
        }
    }
}