package com.tecsup.devtrack.viewmodel

import com.tecsup.devtrack.model.Proyecto

/**
 * Representa el estado observable de la pantalla de proyectos.
 * El ViewModel emite este objeto para que la UI se actualice automáticamente.
 */
data class ProyectoUiState(
    val proyectos: List<Proyecto> = emptyList(),
    val nombre: String = "",
    val descripcion: String = "",

    // Estado seleccionado en el formulario
    val estado: String = "Planificado",

    // Mensaje de validación
    val mensajeError: String = ""
)