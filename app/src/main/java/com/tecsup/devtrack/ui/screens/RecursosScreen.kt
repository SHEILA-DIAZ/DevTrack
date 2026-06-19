package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tecsup.devtrack.viewmodel.RecursosViewModel

/**
 * Pantalla mejorada visualmente para mostrar recursos motivacionales.
 * Incluye imágenes remotas y una interfaz moderna con capas semitransparentes.
 */
@Composable
fun RecursosScreen(
    viewModel: RecursosViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recursos Tecnológicos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarRecurso() }) {
                            Text("Reintentar")
                        }
                    }
                }
                else -> {
                    // Tarjeta inspiracional con imagen de fondo
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Imagen de fondo cargada con Coil
                            AsyncImage(
                                model = uiState.imageUrl,
                                contentDescription = "Fondo motivacional",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Capa semitransparente para legibilidad
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = uiState.frase,
                                        color = Color.White,
                                        style = MaterialTheme.typography.headlineSmall,
                                        textAlign = TextAlign.Center,
                                        fontStyle = FontStyle.Italic,
                                        lineHeight = 32.sp
                                    )
                                    
                                    Spacer(modifier = Modifier.height(20.dp))
                                    
                                    Text(
                                        text = "- ${uiState.autor}",
                                        color = Color.White.copy(alpha = 0.9f),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para actualizar el contenido
        Button(
            onClick = { viewModel.cargarRecurso() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Obtener otra frase inspiradora")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón de navegación
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Volver al Dashboard")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
