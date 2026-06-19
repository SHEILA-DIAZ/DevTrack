package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

/**
 * Pantalla de registro de proyectos (Formulario).
 * Se ha simplificado para contener solo el formulario y mejorar el orden visual.
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding() // COMENTARIO PARA SUSTENTACIÓN: Padding seguro para evitar que el título se tape.
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Registrar proyecto",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Completa la información para organizar tu nuevo proyecto tecnológico.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mensaje de éxito
        if (uiState.mensajeExito.isNotBlank()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = uiState.mensajeExito,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Formulario
        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre del proyecto") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.mensajeError.contains("nombre")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = { viewModel.actualizarDescripcion(it) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            isError = uiState.mensajeError.contains("descripción")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.tecnologias,
            onValueChange = { viewModel.actualizarTecnologias(it) },
            label = { Text("Tecnologías utilizadas") },
            placeholder = { Text("Ej: Kotlin, Compose, Room") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.fechaInicio,
                onValueChange = { viewModel.actualizarFechaInicio(it) },
                label = { Text("Fecha Inicio") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.fechaLimite,
                onValueChange = { viewModel.actualizarFechaLimite(it) },
                label = { Text("Fecha Límite") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = !menuExpandido }
        ) {
            OutlinedTextField(
                value = uiState.estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado del proyecto") },
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

        if (uiState.mensajeError.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = uiState.mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.guardarProyecto() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar proyecto")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
