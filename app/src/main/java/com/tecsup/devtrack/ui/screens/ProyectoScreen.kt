package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

@Composable
fun ProyectoScreen(
    viewModel: ProyectoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "DevTrack",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre del proyecto") },
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

        Button(
            onClick = { viewModel.guardarProyecto() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar proyecto")
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