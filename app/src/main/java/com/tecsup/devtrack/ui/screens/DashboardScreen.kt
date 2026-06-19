package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.model.Tarea

/**
 * Dashboard principal que muestra estadísticas generales del proyecto.
 * Resume el estado de todos los proyectos y tareas registrados.
 */
@Composable
fun DashboardScreen(
    proyectos: List<Proyecto>,
    tareas: List<Tarea>,
    onIrProyectos: () -> Unit,
    onIrRecursos: () -> Unit
) {
    val totalProyectos = proyectos.size
    val enDesarrollo = proyectos.count { it.estado == "En desarrollo" }
    val finalizados = proyectos.count { it.estado == "Finalizado" }
    val planificados = proyectos.count { it.estado == "Planificado" }

    val totalTareas = tareas.size
    val pendientes = tareas.count { it.estado == "Pendiente" }
    val enProceso = tareas.count { it.estado == "En proceso" }
    val completadas = tareas.count { it.estado == "Completada" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("DevTrack", style = MaterialTheme.typography.headlineMedium)

        Text(
            text = "Gestión de proyectos tecnológicos",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Resumen de proyectos",
            style = MaterialTheme.typography.titleLarge
        )

        EstadisticaCard("Total de proyectos", totalProyectos.toString())
        EstadisticaCard("En desarrollo", enDesarrollo.toString())
        EstadisticaCard("Finalizados", finalizados.toString())
        EstadisticaCard("Planificados", planificados.toString())

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Resumen de tareas",
            style = MaterialTheme.typography.titleLarge
        )

        EstadisticaCard("Total de tareas", totalTareas.toString())
        EstadisticaCard("Pendientes", pendientes.toString())
        EstadisticaCard("En proceso", enProceso.toString())
        EstadisticaCard("Completadas", completadas.toString())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onIrProyectos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver proyectos")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onIrRecursos,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Recursos Tecnológicos")
        }
    }
}

@Composable
fun EstadisticaCard(
    titulo: String,
    valor: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}