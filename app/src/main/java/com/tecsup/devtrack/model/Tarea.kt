package com.tecsup.devtrack.model

data class Tarea(
    val id: Int = 0,
    val proyectoId: Int,
    val nombre: String,
    val descripcion: String,
    val estado: String
)