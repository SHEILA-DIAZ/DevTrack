package com.tecsup.devtrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.devtrack.ui.screens.DashboardScreen
import com.tecsup.devtrack.ui.screens.ProyectoScreen
import com.tecsup.devtrack.viewmodel.ProyectoViewModel
import com.tecsup.devtrack.viewmodel.ProyectoViewModelFactory

@Composable
fun AppNavigation(
    factory: ProyectoViewModelFactory
) {
    val navController = rememberNavController()

    val proyectoViewModel: ProyectoViewModel = viewModel(
        factory = factory
    )

    val uiState by proyectoViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                proyectos = uiState.proyectos,
                onIrProyectos = {
                    navController.navigate(Routes.PROYECTOS)
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
    }
}