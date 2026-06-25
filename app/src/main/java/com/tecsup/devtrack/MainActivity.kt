package com.tecsup.devtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.devtrack.data.local.DatabaseProvider
import com.tecsup.devtrack.data.remote.RetrofitClient
import com.tecsup.devtrack.navigation.AppNavigation
import com.tecsup.devtrack.repository.AuthRepository
import com.tecsup.devtrack.repository.ProyectoRepository
import com.tecsup.devtrack.repository.RecursosRepository
import com.tecsup.devtrack.repository.TareaRepository
import com.tecsup.devtrack.ui.theme.DevTrackTheme
import com.tecsup.devtrack.viewmodel.AuthViewModelFactory
import com.tecsup.devtrack.viewmodel.ProyectoViewModelFactory
import com.tecsup.devtrack.viewmodel.RecursosViewModelFactory
import com.tecsup.devtrack.viewmodel.TareaViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database = DatabaseProvider.getDatabase(this)

        // Firebase Auth
        val firebaseAuth = FirebaseAuth.getInstance()
        val authRepository = AuthRepository(firebaseAuth)
        val authFactory = AuthViewModelFactory(authRepository)

        // Repository de proyectos
        val proyectoRepository = ProyectoRepository(
            database.proyectoDao()
        )

        // Repository de tareas
        val tareaRepository = TareaRepository(
            database.tareaDao()
        )

        // Repository de recursos (Retrofit)
        val recursosRepository = RecursosRepository(
            RetrofitClient.instance
        )

        // Factory de proyectos
        val proyectoFactory = ProyectoViewModelFactory(
            proyectoRepository
        )

        // Factory de tareas
        val tareaFactory = TareaViewModelFactory(
            tareaRepository
        )

        // Factory de recursos
        val recursosFactory = RecursosViewModelFactory(
            recursosRepository
        )

        setContent {
            DevTrackTheme {
                AppNavigation(
                    factory = proyectoFactory,
                    tareaFactory = tareaFactory,
                    recursosFactory = recursosFactory,
                    authFactory = authFactory
                )
            }
        }
    }
}