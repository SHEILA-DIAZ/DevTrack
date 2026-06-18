package com.tecsup.devtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.devtrack.data.local.DatabaseProvider
import com.tecsup.devtrack.navigation.AppNavigation
import com.tecsup.devtrack.repository.ProyectoRepository
import com.tecsup.devtrack.repository.TareaRepository
import com.tecsup.devtrack.ui.theme.DevTrackTheme
import com.tecsup.devtrack.viewmodel.ProyectoViewModelFactory
import com.tecsup.devtrack.viewmodel.TareaViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database = DatabaseProvider.getDatabase(this)

        // Repository de proyectos
        val proyectoRepository = ProyectoRepository(
            database.proyectoDao()
        )

        // Repository de tareas
        val tareaRepository = TareaRepository(
            database.tareaDao()
        )

        // Factory de proyectos
        val proyectoFactory = ProyectoViewModelFactory(
            proyectoRepository
        )

        // Factory de tareas
        val tareaFactory = TareaViewModelFactory(
            tareaRepository
        )

        setContent {
            DevTrackTheme {
                AppNavigation(
                    factory = proyectoFactory,
                    tareaFactory = tareaFactory
                )
            }
        }
    }
}