package com.tecsup.devtrack.model

/**
 * Modelo de negocio que representa una Tarea asociada a un proyecto.
 */
data class Tarea(
    val id: Int = 0,
    val proyectoId: Int,
    val nombre: String,
    val descripcion: String,
    val estado: String
)