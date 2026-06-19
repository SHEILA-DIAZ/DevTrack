package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * Pantalla de bienvenida principal (Splash) de DevTrack.
 * COMENTARIO PARA SUSTENTACIÓN: Esta pantalla es el punto de entrada de la aplicación.
 * Centraliza la presentación del producto y las opciones de acceso inicial.
 * En la Parte 2, aquí se integrará Firebase para decidir si mostrar el login o entrar directo.
 */
@Composable
fun SplashScreen(
    onNavegarAlLogin: () -> Unit,
    onNavegarAlRegistro: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título principal con énfasis visual
        Text(
            text = "Bienvenido a DevTrack",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtítulo con la propuesta de valor de la app
        Text(
            text = "Organiza, gestiona y da seguimiento a tus proyectos tecnológicos desde un solo lugar.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Contenedor de imagen referencial
        // COMENTARIO PARA SUSTENTACIÓN: Se utiliza AsyncImage de Coil para cargar contenido dinámico
        // que refuerza visualmente el propósito tecnológico de la herramienta.
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = "https://i.pinimg.com/736x/9f/06/8b/9f068b54092b1089f8d016f02a585e70.jpg",
                contentDescription = "Gestión de Proyectos",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Acciones principales de la pantalla inicial
        // COMENTARIO PARA SUSTENTACIÓN: Los botones están desacoplados de la lógica de navegación
        // mediante callbacks, siguiendo las mejores prácticas de Compose.
        Button(
            onClick = onNavegarAlLogin,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onNavegarAlRegistro,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Registrarse")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
