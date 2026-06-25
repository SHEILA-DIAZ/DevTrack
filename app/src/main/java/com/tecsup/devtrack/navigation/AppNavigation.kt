package com.tecsup.devtrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.devtrack.ui.screens.DashboardScreen
import com.tecsup.devtrack.ui.screens.DetalleProyectoScreen
import com.tecsup.devtrack.ui.screens.ListaProyectosScreen
import com.tecsup.devtrack.ui.screens.LoginScreen
import com.tecsup.devtrack.ui.screens.ProfileScreen
import com.tecsup.devtrack.ui.screens.ProyectoScreen
import com.tecsup.devtrack.ui.screens.RecursosScreen
import com.tecsup.devtrack.ui.screens.RegistroScreen
import com.tecsup.devtrack.ui.screens.SplashScreen
import com.tecsup.devtrack.ui.screens.TareaScreen
import com.tecsup.devtrack.viewmodel.*

/**
 * Configuración del NavHost para gestionar la navegación entre pantallas.
 * Define cómo se pasan parámetros, como el proyectoId, entre destinos.
 */
@Composable
fun AppNavigation(
    factory: ProyectoViewModelFactory,
    tareaFactory: TareaViewModelFactory,
    recursosFactory: RecursosViewModelFactory,
    authFactory: AuthViewModelFactory
) {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel(factory = authFactory)
    val authUiState by authViewModel.uiState.collectAsState()

    val proyectoViewModel: ProyectoViewModel = viewModel(
        factory = factory
    )

    val tareaViewModel: TareaViewModel = viewModel(
        factory = tareaFactory
    )

    // COMENTARIO PARA SUSTENTACIÓN: Sincronización global del estado del usuario.
    // Cuando el ID del usuario cambia (Login/Logout), reiniciamos la carga de datos.
    LaunchedEffect(authUiState.userId) {
        val uid = authUiState.userId
        if (uid != null) {
            proyectoViewModel.cargarDatosUsuario(uid)
            tareaViewModel.cargarDatosUsuario(uid)
        } else {
            proyectoViewModel.limpiarDatos()
            tareaViewModel.limpiarDatos()
        }
    }

    val recursosViewModel: RecursosViewModel = viewModel(
        factory = recursosFactory
    )

    val proyectoUiState by proyectoViewModel.uiState.collectAsState()
    val tareaUiState by tareaViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavegarAlLogin = {
                    navController.navigate(Routes.LOGIN)
                },
                onNavegarAlRegistro = {
                    navController.navigate(Routes.REGISTRO)
                },
                isAuthenticated = authUiState.isAuthenticated,
                onNavigateToDashboard = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onNavegarAlRegistro = {
                    navController.navigate(Routes.REGISTRO)
                },
                onIngresarAlDashboard = {
                    navController.navigate(Routes.DASHBOARD) {
                        // Limpiamos el historial para que no pueda volver al Login con el botón atrás
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTRO) {
            RegistroScreen(
                viewModel = authViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onNavegarAlLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTRO) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.REGISTRO) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                proyectos = proyectoUiState.proyectos,
                tareas = tareaUiState.tareas,
                onNavegar = { ruta ->
                    navController.navigate(ruta)
                },
                onAgregarProyecto = {
                    proyectoViewModel.limpiarFormulario()
                    navController.navigate(Routes.PROYECTOS)
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = authViewModel,
                onVolver = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.SPLASH) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DETALLE_PROYECTO) {
            DetalleProyectoScreen(onVolver = { navController.popBackStack() })
        }

        composable(Routes.RECURSOS) {
            RecursosScreen(
                viewModel = recursosViewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PROYECTOS) {
            ProyectoScreen(
                viewModel = proyectoViewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.LISTA_PROYECTOS) {
            ListaProyectosScreen(
                viewModel = proyectoViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onVerTareas = { proyectoId ->
                    navController.navigate(Routes.tareas(proyectoId))
                },
                onEditarProyecto = {
                    navController.navigate(Routes.PROYECTOS)
                }
            )
        }

        composable(
            route = Routes.TAREAS,
            arguments = listOf(
                navArgument("proyectoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val proyectoId = backStackEntry.arguments?.getInt("proyectoId") ?: 0

            TareaScreen(
                viewModel = tareaViewModel,
                proyectoId = proyectoId,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}