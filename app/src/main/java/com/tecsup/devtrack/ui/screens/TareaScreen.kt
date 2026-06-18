package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.viewmodel.TareaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaScreen(
    viewModel: TareaViewModel,
    proyectoId: Int,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var menuExpandido by remember { mutableStateOf(false) }

    val estados = listOf(
        "Pendiente",
        "En proceso",
        "Completada"
    )

    LaunchedEffect(proyectoId) {
        viewModel.cargarTareasPorProyecto(proyectoId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tareas del proyecto",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a proyectos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = { viewModel.actualizarDescripcion(it) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
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
                label = { Text("Estado de la tarea") },
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
            onClick = { viewModel.guardarTarea() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lista de tareas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(uiState.tareas) { tarea ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(text = tarea.nombre)
                        Text(text = tarea.descripcion)
                        Text(text = tarea.estado)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.seleccionarTarea(tarea)
                                }
                            ) {
                                Text("Editar")
                            }

                            Button(
                                onClick = {
                                    viewModel.eliminarTarea(tarea)
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