package com.tecsup.devtrack.viewmodel

import com.tecsup.devtrack.model.Tarea

/**
 * Estado de la UI para la gestión de tareas de un proyecto específico.
 */
data class TareaUiState(
    val tareas: List<Tarea> = emptyList(),
    val nombre: String = "",
    val descripcion: String = "",
    val estado: String = "Pendiente",
    val proyectoId: Int = 0,
    val mensajeError: String = ""
)