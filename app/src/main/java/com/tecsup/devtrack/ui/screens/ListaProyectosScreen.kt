package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

/**
 * Pantalla que muestra la lista de proyectos registrados.
 * Separada del formulario para mejorar la organización visual.
 */
@Composable
fun ListaProyectosScreen(
    viewModel: ProyectoViewModel,
    onVolver: () -> Unit,
    onVerTareas: (Int) -> Unit,
    onEditarProyecto: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding() // Padding seguro para la barra de estado
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Lista de proyectos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Gestiona tus proyectos tecnológicos registrados.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.proyectos.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "No hay proyectos registrados todavía.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            uiState.proyectos.forEach { proyecto ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = proyecto.nombre, 
                            fontWeight = FontWeight.Bold, 
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = proyecto.descripcion, 
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2
                        )
                        Text(
                            text = "Estado: ${proyecto.estado}", 
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(), 
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onVerTareas(proyecto.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Tareas")
                            }
                            OutlinedButton(
                                onClick = { 
                                    viewModel.seleccionarProyecto(proyecto)
                                    onEditarProyecto()
                                }
                            ) {
                                Text("Editar")
                            }
                            IconButton(onClick = { viewModel.eliminarProyecto(proyecto) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Dashboard")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
