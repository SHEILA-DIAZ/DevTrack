package com.tecsup.devtrack.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tecsup.devtrack.MainActivity
import com.tecsup.devtrack.R

/**
 * Servicio para gestionar notificaciones push de Firebase (FCM).
 * COMENTARIO PARA SUSTENTACIÓN: Permite recibir mensajes en primer y segundo plano,
 * gestionando el canal de notificaciones para Android 8+ y tokens de dispositivo.
 */
class DevTrackFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // COMENTARIO PARA SUSTENTACIÓN: El token identifica este dispositivo en Firebase Console.
        Log.d("FCM_TOKEN_DEVTRACK", "Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // COMENTARIO PARA SUSTENTACIÓN: Se activa cuando llega una notificación con la app abierta.
        remoteMessage.notification?.let {
            showNotification(it.title ?: "DevTrack", it.body ?: "")
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "devtrack_notifications"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Configuración del canal para Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "DevTrack Notificaciones",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intento para abrir la app al tocar la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construcción de la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Usando el icono por defecto
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }
}
