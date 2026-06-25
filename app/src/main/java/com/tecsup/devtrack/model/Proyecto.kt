package com.tecsup.devtrack.model

/**
 * Modelo de negocio que representa un Proyecto en la aplicación.
 * Se utiliza para transportar datos entre la UI y el repositorio.
 */
data class Proyecto(
    val id: Int = 0,
    val userId: String = "", // COMENTARIO PARA SUSTENTACIÓN: Identificador del usuario propietario en Firebase
    val nombre: String = "",
    val descripcion: String = "",
    val tecnologias: String = "",
    val estado: String = "",
    val fechaInicio: String = "",
    val fechaLimite: String = "",
    val observaciones: String = ""
)
