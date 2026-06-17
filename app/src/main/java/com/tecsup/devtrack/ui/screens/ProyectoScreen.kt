package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.model.Proyecto

@Composable
fun ProyectoScreen() {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var proyectos by remember { mutableStateOf(listOf<Proyecto>()) }

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
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del proyecto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && descripcion.isNotBlank()) {
                    proyectos = proyectos + Proyecto(
                        id = proyectos.size + 1,
                        nombre = nombre,
                        descripcion = descripcion,
                        tecnologias = "Kotlin",
                        estado = "En desarrollo",
                        fechaInicio = "2026-01-01",
                        fechaLimite = "2026-12-31",
                        observaciones = "Proyecto registrado en DevTrack"
                    )
                    nombre = ""
                    descripcion = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar proyecto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lista de proyectos",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn {
            items(proyectos) { proyecto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = proyecto.nombre)
                        Text(text = proyecto.descripcion)
                        Text(text = proyecto.estado)
                    }
                }
            }
        }
    }
}