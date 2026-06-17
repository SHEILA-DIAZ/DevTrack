package com.tecsup.devtrack.model

data class Proyecto(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val tecnologias: String = "",
    val estado: String = "",
    val fechaInicio: String = "",
    val fechaLimite: String = "",
    val observaciones: String = ""
)