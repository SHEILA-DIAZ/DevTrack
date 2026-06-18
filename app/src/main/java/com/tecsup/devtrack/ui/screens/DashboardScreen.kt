package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.model.Proyecto

@Composable
fun DashboardScreen(
    proyectos: List<Proyecto>,
    onIrProyectos: () -> Unit
) {
    val total = proyectos.size
    val enDesarrollo = proyectos.count { it.estado == "En desarrollo" }
    val finalizados = proyectos.count { it.estado == "Finalizado" }
    val planificados = proyectos.count { it.estado == "Planificado" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("DevTrack", style = MaterialTheme.typography.headlineMedium)

        Text(
            text = "Gestión de proyectos tecnológicos",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        EstadisticaCard("Total de proyectos", total.toString())
        EstadisticaCard("En desarrollo", enDesarrollo.toString())
        EstadisticaCard("Finalizados", finalizados.toString())
        EstadisticaCard("Planificados", planificados.toString())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onIrProyectos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver proyectos")
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