package com.tecsup.devtrack.viewmodel

import com.tecsup.devtrack.model.Tarea

data class TareaUiState(
    val tareas: List<Tarea> = emptyList(),
    val nombre: String = "",
    val descripcion: String = "",
    val estado: String = "Pendiente",
    val proyectoId: Int = 0
)