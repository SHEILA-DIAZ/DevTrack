package com.tecsup.devtrack.viewmodel

import com.tecsup.devtrack.model.Proyecto

/**
 * Representa el estado observable de la pantalla de proyectos.
 */
data class ProyectoUiState(
    val proyectos: List<Proyecto> = emptyList(),
    val nombre: String = "",
    val descripcion: String = "",
    val tecnologias: String = "",
    val fechaInicio: String = "",
    val fechaLimite: String = "",
    val estado: String = "Planificado",
    
    val mensajeError: String = "",
    val mensajeExito: String = ""
)
