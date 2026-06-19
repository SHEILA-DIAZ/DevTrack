package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.tecsup.devtrack.viewmodel.TareaViewModel

/**
 * Pantalla que gestiona las tareas de un proyecto o muestra la lista general.
 * COMENTARIO PARA SUSTENTACIÓN: Esta pantalla es polivalente. Implementa un diseño premium SaaS
 * con tarjetas de resumen dinámico y una lista reactiva conectada al TareaViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaScreen(
    viewModel: TareaViewModel,
    proyectoId: Int,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var menuExpandido by remember { mutableStateOf(false) }

    val estados = listOf("Pendiente", "En proceso", "Completada")

    // Cálculo de estadísticas locales para el resumen superior
    val total = uiState.tareas.size
    val pendientes = uiState.tareas.count { it.estado == "Pendiente" }
    val enProceso = uiState.tareas.count { it.estado == "En proceso" }
    val completadas = uiState.tareas.count { it.estado == "Completada" }

    LaunchedEffect(proyectoId) {
        if (proyectoId > 0) {
            viewModel.cargarTareasPorProyecto(proyectoId)
        } else {
            viewModel.cargarTodasLasTareas()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Decoraciones de fondo difuminadas consistentes con el estilo DevTrack
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = 150.dp, y = (-50).dp)
                .background(Color(0xFF4B6CB7).copy(alpha = 0.05f), CircleShape)
                .blur(50.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Encabezado Premium
            Text(
                text = if (proyectoId == 0) "Todas las tareas" else "Tareas del proyecto",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1C1E)
            )
            Text(
                text = "Visualiza y gestiona las actividades registradas en tus proyectos.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Resumen superior visual que facilita la lectura de KPIs.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ResumenItem("Total", total.toString(), Color(0xFF4B6CB7))
                    ResumenItem("Pend.", pendientes.toString(), Color(0xFF757575))
                    ResumenItem("Proc.", enProceso.toString(), Color(0xFFF9A825))
                    ResumenItem("Comp.", completadas.toString(), Color(0xFF2E7D32))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (proyectoId > 0) {
                // Formulario minimalista para agregar/editar (solo en modo proyecto)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F3F4))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = uiState.nombre,
                            onValueChange = { viewModel.actualizarNombre(it) },
                            label = { Text("Nombre de la tarea") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = uiState.mensajeError.isNotBlank() && uiState.nombre.isBlank()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.descripcion,
                            onValueChange = { viewModel.actualizarDescripcion(it) },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = uiState.mensajeError.isNotBlank() && uiState.descripcion.isBlank()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(
                            expanded = menuExpandido,
                            onExpandedChange = { menuExpandido = !menuExpandido }
                        ) {
                            OutlinedTextField(
                                value = uiState.estado,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Estado") },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(expanded = menuExpandido, onDismissRequest = { menuExpandido = false }) {
                                estados.forEach { estado ->
                                    DropdownMenuItem(
                                        text = { Text(estado) },
                                        onClick = { viewModel.actualizarEstado(estado); menuExpandido = false }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.guardarTarea() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7))
                        ) {
                            Text("Guardar tarea")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Lista de tareas
            LazyColumn(modifier = Modifier.weight(1f)) {
                if (uiState.tareas.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(32.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Aún no tienes tareas registradas.", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                Text("Crea tareas desde un proyecto para visualizar su avance.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, textAlign = TextAlign.Center)
                            }
                        }
                    }
                } else {
                    items(uiState.tareas) { tarea ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = tarea.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                                    if (proyectoId == 0) {
                                        Surface(color = Color(0xFFF1F3F4), shape = RoundedCornerShape(8.dp)) {
                                            Text("P#${tarea.proyectoId}", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                        }
                                    }
                                }
                                Text(text = tarea.descripcion, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                val statusColor = when(tarea.estado) {
                                    "Completada" -> Color(0xFF2E7D32)
                                    "En proceso" -> Color(0xFFF9A825)
                                    else -> Color(0xFF757575)
                                }
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(color = statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                                        Text(text = tarea.estado, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = { viewModel.seleccionarTarea(tarea) }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { viewModel.eliminarTarea(tarea) }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D3E9F))
            ) {
                Text("Volver al Dashboard", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ResumenItem(label: String, valor: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}
