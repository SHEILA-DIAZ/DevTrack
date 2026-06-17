package com.tecsup.devtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.devtrack.ui.screens.ProyectoScreen
import com.tecsup.devtrack.ui.theme.DevTrackTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DevTrackTheme {
                ProyectoScreen()
            }
        }
    }
}