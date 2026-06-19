package com.tecsup.devtrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.devtrack.ui.screens.DashboardScreen
import com.tecsup.devtrack.ui.screens.ProyectoScreen
import com.tecsup.devtrack.ui.screens.RecursosScreen
import com.tecsup.devtrack.ui.screens.TareaScreen
import com.tecsup.devtrack.viewmodel.ProyectoViewModel
import com.tecsup.devtrack.viewmodel.ProyectoViewModelFactory
import com.tecsup.devtrack.viewmodel.RecursosViewModel
import com.tecsup.devtrack.viewmodel.RecursosViewModelFactory
import com.tecsup.devtrack.viewmodel.TareaViewModel
import com.tecsup.devtrack.viewmodel.TareaViewModelFactory

/**
 * Configuración del NavHost para gestionar la navegación entre pantallas.
 * Define cómo se pasan parámetros, como el proyectoId, entre destinos.
 */
@Composable
fun AppNavigation(
    factory: ProyectoViewModelFactory,
    tareaFactory: TareaViewModelFactory,
    recursosFactory: RecursosViewModelFactory
) {
    val navController = rememberNavController()

    val proyectoViewModel: ProyectoViewModel = viewModel(
        factory = factory
    )

    val tareaViewModel: TareaViewModel = viewModel(
        factory = tareaFactory
    )

    val recursosViewModel: RecursosViewModel = viewModel(
        factory = recursosFactory
    )

    val proyectoUiState by proyectoViewModel.uiState.collectAsState()
    val tareaUiState by tareaViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                proyectos = proyectoUiState.proyectos,
                tareas = tareaUiState.tareas,
                onIrProyectos = {
                    navController.navigate(Routes.PROYECTOS)
                },
                onIrRecursos = {
                    navController.navigate(Routes.RECURSOS)
                }
            )
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
                },
                onVerTareas = { proyectoId ->
                    navController.navigate(Routes.tareas(proyectoId))
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