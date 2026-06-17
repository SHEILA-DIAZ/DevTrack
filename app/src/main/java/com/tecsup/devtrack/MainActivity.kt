package com.tecsup.devtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.devtrack.ui.screens.ProyectoScreen
import com.tecsup.devtrack.ui.theme.DevTrackTheme
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DevTrackTheme {
                val proyectoViewModel: ProyectoViewModel = viewModel()
                ProyectoScreen(viewModel = proyectoViewModel)
            }
        }
    }
}