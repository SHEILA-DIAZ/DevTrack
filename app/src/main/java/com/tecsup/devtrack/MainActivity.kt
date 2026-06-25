package com.tecsup.devtrack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
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

    // Registra el lanzador para solicitar permiso de notificaciones (Android 13+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM_PERMISSION", "Permiso de notificaciones concedido")
        } else {
            Log.d("FCM_PERMISSION", "Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        askNotificationPermission()
        getAndLogFcmToken()

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

    private fun askNotificationPermission() {
        // COMENTARIO PARA SUSTENTACIÓN: Solicita permiso de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("FCM_PERMISSION", "Permiso ya concedido")
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun getAndLogFcmToken() {
        // COMENTARIO PARA SUSTENTACIÓN: Recupera el token FCM para pruebas desde la consola de Firebase.
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN_DEVTRACK", "Fallo al obtener el token", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM_TOKEN_DEVTRACK", "Token actual: $token")
        }
    }
}