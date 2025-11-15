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
        startDestination = Route.Splash.definition
    ) {
        composable(Route.Splash.definition) {
            SplashScreen(onTimeout = {
                navController.navigate(Route.Login.definition) {
                    popUpTo(Route.Splash.definition) { inclusive = true }
                }
            })
        }
        composable(Route.Login.definition) { 
            LoginScreen(
                onLoginOkNavigateHome = { userName, userId ->
                    navController.navigate(Route.Home.build(userName, userId)) {
                        popUpTo(Route.Login.definition) { inclusive = true }
                    }
                },
                onDoctorLoginSuccess = { userName, medicoId ->
                    navController.navigate(Route.DoctorHome.build(userName, medicoId)) {
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
                navArgument("userId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L

            HomeScreen(
                userName = userName,
                userId = userId,
                onLogout = {
                    navController.navigate(Route.Login.definition) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onGoToRequestAppointment = { pId ->
                    navController.navigate(Route.RequestAppointment.build(pId))
                },
                onGoToMyAppointments = { pId ->
                    navController.navigate(Route.MyAppointments.build(pId))
                },
                onGoToCamera = { pId -> navController.navigate(Route.Camera.build(pId)) },
                onGoToMyRecipes = { pId -> navController.navigate(Route.MyRecipes.build(pId)) } // Added navigation to gallery
            )
        }
        composable(
            route = Route.DoctorHome.definition,
            arguments = listOf(
                navArgument("userName") { type = NavType.StringType },
                navArgument("medicoId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val medicoId = backStackEntry.arguments?.getLong("medicoId") ?: -1L

            DoctorHomeScreen(
                medicoId = medicoId,
                userName = userName,
                onLogout = {
                    navController.navigate(Route.Login.definition) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onAppointmentClick = { citaId ->
                    navController.navigate(Route.DoctorAppointmentDetail.build(citaId))
                }
            )
        }
        composable(
            route = Route.DoctorAppointmentDetail.definition,
            arguments = listOf(navArgument("citaId") { type = NavType.LongType })
        ) {
            DoctorAppointmentDetailScreen(onBack = { navController.popBackStack() })
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
            val specialtyId = backStackEntry.arguments?.getLong("specialtyId") ?: -1L
            val pacienteId = backStackEntry.arguments?.getLong("pacienteId") ?: -1L
            SelectDoctorScreen(
                specialtyId = specialtyId,
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
        composable(
            route = Route.MyAppointments.definition,
            arguments = listOf(navArgument("patientId") { type = NavType.LongType })
        ) { 
            MyAppointmentsScreen(
                onGoToAppointmentDetail = { citaId ->
                    navController.navigate(Route.AppointmentDetail.build(citaId))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Route.AppointmentDetail.definition,
            arguments = listOf(navArgument("citaId") { type = NavType.LongType })
        ) {
            // The AppointmentDetailScreen is missing, so I will comment this out for now
            // AppointmentDetailScreen(
            //     onBack = { navController.popBackStack() }
            // )
        }

        composable(
            route = Route.Camera.definition,
            arguments = listOf(navArgument("patientId") { type = NavType.LongType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getLong("patientId") ?: -1L
            CameraScreen(
                patientId = patientId,
                onPhotoSaved = { navController.popBackStack() } // Navigate back after saving
            )
        }

        composable(
            route = Route.MyRecipes.definition,
            arguments = listOf(navArgument("patientId") { type = NavType.LongType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getLong("patientId") ?: -1L
            RecetasScreen(
                pacienteId = patientId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}