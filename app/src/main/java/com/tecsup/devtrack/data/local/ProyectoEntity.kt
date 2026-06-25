package com.tecsup.devtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa la tabla "proyectos" en la base de datos local de Room.
 */
@Entity(tableName = "proyectos")
data class ProyectoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String, // COMENTARIO PARA SUSTENTACIÓN: Permite filtrar datos locales por usuario autenticado
    val nombre: String,
    val descripcion: String,
    val tecnologias: String,
    val estado: String,
    val fechaInicio: String,
    val fechaLimite: String,
    val observaciones: String
)
