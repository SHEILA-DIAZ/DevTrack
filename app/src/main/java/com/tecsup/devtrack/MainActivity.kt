package com.tecsup.devtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.devtrack.data.local.DatabaseProvider
import com.tecsup.devtrack.repository.ProyectoRepository
import com.tecsup.devtrack.ui.screens.ProyectoScreen
import com.tecsup.devtrack.ui.theme.DevTrackTheme
import com.tecsup.devtrack.viewmodel.ProyectoViewModel
import com.tecsup.devtrack.viewmodel.ProyectoViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database = DatabaseProvider.getDatabase(this)

        val repository = ProyectoRepository(
            database.proyectoDao()
        )

        val factory = ProyectoViewModelFactory(repository)

        setContent {
            DevTrackTheme {
                val proyectoViewModel: ProyectoViewModel = viewModel(
                    factory = factory
                )

                ProyectoScreen(
                    viewModel = proyectoViewModel
                )
            }
        }
    }
}