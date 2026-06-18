package com.tecsup.devtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proyectos")
data class ProyectoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val tecnologias: String,
    val estado: String,
    val fechaInicio: String,
    val fechaLimite: String,
    val observaciones: String
)