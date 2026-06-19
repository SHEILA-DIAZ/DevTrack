package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.viewmodel.TareaViewModel

/**
 * Pantalla que gestiona las tareas de un proyecto o muestra la lista general.
 * COMENTARIO PARA SUSTENTACIÓN: Esta pantalla es polivalente. Si recibe un proyectoId > 0,
 * permite el CRUD. Si recibe 0, funciona como una vista general de todas las tareas.
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

    LaunchedEffect(proyectoId) {
        if (proyectoId > 0) {
            viewModel.cargarTareasPorProyecto(proyectoId)
        } else {
            viewModel.cargarTodasLasTareas()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = if (proyectoId == 0) "Todas las tareas" else "Tareas del proyecto",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // COMENTARIO PARA SUSTENTACIÓN: El formulario solo se muestra cuando se gestiona un proyecto específico.
        if (proyectoId > 0) {
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.actualizarNombre(it) },
                label = { Text("Nombre de la tarea") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.mensajeError.isNotBlank() && uiState.nombre.isBlank()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.actualizarDescripcion(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
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
                    label = { Text("Estado de la tarea") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.guardarTarea() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar tarea")
            }

            if (uiState.mensajeError.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tareas registradas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
        }

        // COMENTARIO PARA SUSTENTACIÓN: Lista reactiva de tareas. 
        // Si proyectoId es 0, muestra todas las tareas cargadas en el ViewModel.
        if (uiState.tareas.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Aún no tienes tareas registradas.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.tareas) { tarea ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = tarea.nombre, fontWeight = FontWeight.Bold)
                            Text(text = tarea.descripcion)
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Estado: ${tarea.estado}",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                                // Si es la vista general, mostramos el ID del proyecto asociado
                                if (proyectoId == 0) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "ID Proy: ${tarea.proyectoId}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }

                            // Opciones de edición/eliminación solo disponibles en vista de proyecto o si se desea generalizar
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.seleccionarTarea(tarea) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Editar")
                                }

                                IconButton(onClick = { viewModel.eliminarTarea(tarea) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // COMENTARIO PARA SUSTENTACIÓN: Botón de retorno posicionado al final para mejorar la ergonomía.
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Dashboard")
        }
    }
}
