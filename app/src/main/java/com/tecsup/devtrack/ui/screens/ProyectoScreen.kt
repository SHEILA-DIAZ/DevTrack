package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

/**
 * Pantalla principal de proyectos.
 * Permite registrar, editar y listar proyectos, enviando acciones al ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoScreen(
    viewModel: ProyectoViewModel,
    onVolver: () -> Unit,
    onVerTareas: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var menuExpandido by remember { mutableStateOf(false) }

    val estados = listOf(
        "Planificado",
        "En desarrollo",
        "Finalizado"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "DevTrack",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Dashboard")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre del proyecto") },
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
            onExpandedChange = {
                menuExpandido = !menuExpandido
            }
        ) {
            OutlinedTextField(
                value = uiState.estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado del proyecto") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = {
                    menuExpandido = false
                }
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
            onClick = { viewModel.guardarProyecto() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar proyecto")
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
            text = "Lista de proyectos",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(uiState.proyectos) { proyecto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(text = proyecto.nombre)
                        Text(text = proyecto.descripcion)
                        Text(text = proyecto.estado)

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                onVerTareas(proyecto.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver tareas")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.seleccionarProyecto(proyecto)
                                }
                            ) {
                                Text("Editar")
                            }

                            Button(
                                onClick = {
                                    viewModel.eliminarProyecto(proyecto)
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}