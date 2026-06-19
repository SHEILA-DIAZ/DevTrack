package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

/**
 * Pantalla de registro de proyectos de DevTrack rediseñada con estilo Premium.
 * COMENTARIO PARA SUSTENTACIÓN: Esta pantalla sigue la identidad visual SaaS de la app,
 * organizando el formulario en una tarjeta elevada para mejorar la usabilidad.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoScreen(
    viewModel: ProyectoViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var menuExpandido by remember { mutableStateOf(false) }

    val estados = listOf("Planificado", "En desarrollo", "Finalizado")

    // COMENTARIO PARA SUSTENTACIÓN: Fondo consistente con la línea visual de la aplicación.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Decoraciones de fondo difuminadas
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .background(Color(0xFF4B6CB7).copy(alpha = 0.05f), CircleShape)
                .blur(40.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Encabezado con identidad de marca.
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
                        text = "Registrar proyecto",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1C1E)
                    )
                    Text(
                        text = "Organiza y planifica tus proyectos tecnológicos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Mensaje de éxito visualmente destacado.
            if (uiState.mensajeExito.isNotBlank()) {
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.mensajeExito,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // COMENTARIO PARA SUSTENTACIÓN: Tarjeta principal blanca para el formulario.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo: Nombre
                    OutlinedTextField(
                        value = uiState.nombre,
                        onValueChange = { viewModel.actualizarNombre(it) },
                        label = { Text("Nombre del proyecto") },
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.mensajeError.contains("nombre")
                    )

                    // Campo: Descripción
                    OutlinedTextField(
                        value = uiState.descripcion,
                        onValueChange = { viewModel.actualizarDescripcion(it) },
                        label = { Text("Descripción") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.mensajeError.contains("descripción")
                    )

                    // Campo: Tecnologías
                    OutlinedTextField(
                        value = uiState.tecnologias,
                        onValueChange = { viewModel.actualizarTecnologias(it) },
                        label = { Text("Tecnologías utilizadas") },
                        leadingIcon = { Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                        placeholder = { Text("Ej: Kotlin, Compose, Room") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Campo: Fecha Inicio
                        OutlinedTextField(
                            value = uiState.fechaInicio,
                            onValueChange = { viewModel.actualizarFechaInicio(it) },
                            label = { Text("Inicio") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("AAAA-MM-DD") }
                        )
                        // Campo: Fecha Fin
                        OutlinedTextField(
                            value = uiState.fechaLimite,
                            onValueChange = { viewModel.actualizarFechaLimite(it) },
                            label = { Text("Límite") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("AAAA-MM-DD") }
                        )
                    }

                    // Campo: Estado (Dropdown)
                    ExposedDropdownMenuBox(
                        expanded = menuExpandido,
                        onExpandedChange = { menuExpandido = !menuExpandido }
                    ) {
                        OutlinedTextField(
                            value = uiState.estado,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Estado del proyecto") },
                            leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = menuExpandido,
                            onDismissRequest = { menuExpandido = false }
                        ) {
                            estados.forEach { estado ->
                                DropdownMenuItem(
                                    text = { Text(estado) },
                                    onClick = {
                                        viewModel.actualizarEstado(estado)
                                        menuExpandido = false
                                    }
                                )
                            }
                        }
                    }

                    if (uiState.mensajeError.isNotBlank()) {
                        Text(
                            text = uiState.mensajeError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // COMENTARIO PARA SUSTENTACIÓN: Botón principal con diseño premium.
                    Button(
                        onClick = { viewModel.guardarProyecto() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar proyecto", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Volver Outlined
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF4B6CB7)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4B6CB7))
            ) {
                Text("Volver al Dashboard", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
