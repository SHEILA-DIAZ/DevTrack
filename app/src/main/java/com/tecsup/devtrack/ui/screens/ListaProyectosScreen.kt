package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

/**
 * Pantalla de lista de proyectos de DevTrack rediseñada con estilo Premium.
 * COMENTARIO PARA SUSTENTACIÓN: Esta pantalla organiza los proyectos existentes en tarjetas interactivas,
 * permitiendo una gestión rápida y visual del progreso tecnológico.
 */
@Composable
fun ListaProyectosScreen(
    viewModel: ProyectoViewModel,
    onVolver: () -> Unit,
    onVerTareas: (Int) -> Unit,
    onEditarProyecto: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val totalProyectos = uiState.proyectos.size

    // COMENTARIO PARA SUSTENTACIÓN: Fondo consistente con la línea visual SaaS de la app.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Decoraciones de fondo difuminadas
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = 150.dp, y = (-100).dp)
                .background(Color(0xFF4B6CB7).copy(alpha = 0.05f), CircleShape)
                .blur(50.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Encabezado con jerarquía visual y branding "DT".
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF4B6CB7),
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("DT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Lista de proyectos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1C1E)
                    )
                    Text(
                        text = "Gestiona y supervisa todos tus proyectos tecnológicos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Resumen superior dinámico que muestra la carga de trabajo actual.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4B6CB7).copy(alpha = 0.08f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF4B6CB7))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Actualmente gestionas $totalProyectos proyectos.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B6CB7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.proyectos.isEmpty()) {
                // Estado vacío elegante
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Aún no tienes proyectos registrados.", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("Comienza agregando uno desde el Dashboard.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }
            } else {
                // COMENTARIO PARA SUSTENTACIÓN: Renderizado dinámico de tarjetas de proyecto con estados visuales.
                uiState.proyectos.forEach { proyecto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFF4B6CB7), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = proyecto.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1C1E)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = proyecto.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Tags de tecnologías y fecha
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                if (proyecto.tecnologias.isNotBlank()) {
                                    Surface(
                                        color = Color(0xFFF1F3F4),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = proyecto.tecnologias,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                                if (proyecto.fechaLimite.isNotBlank()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = proyecto.fechaLimite, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // COMENTARIO PARA SUSTENTACIÓN: Uso de chips visuales para diferenciar el estado del proyecto.
                            val (estadoColor, estadoIcono) = when (proyecto.estado) {
                                "Finalizado" -> Color(0xFF2E7D32) to Icons.Default.CheckCircle
                                "En desarrollo" -> Color(0xFFF9A825) to Icons.Default.Build
                                else -> Color(0xFF1976D2) to Icons.Default.DateRange
                            }

                            Surface(
                                color = estadoColor.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, estadoColor.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(estadoIcono, contentDescription = null, modifier = Modifier.size(16.dp), tint = estadoColor)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = proyecto.estado, color = estadoColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // COMENTARIO PARA SUSTENTACIÓN: Acciones organizadas por jerarquía (Primaria, Secundaria, Peligro).
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { onVerTareas(proyecto.id) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7))
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Tareas", fontSize = 14.sp)
                                }
                                
                                OutlinedButton(
                                    onClick = { 
                                        viewModel.seleccionarProyecto(proyecto)
                                        onEditarProyecto()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.Gray)
                                }

                                IconButton(
                                    onClick = { viewModel.eliminarProyecto(proyecto) },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFFEBEE))
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Volver Premium
            Button(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D3E9F)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Volver al Dashboard", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
