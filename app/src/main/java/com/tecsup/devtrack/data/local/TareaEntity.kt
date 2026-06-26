package com.tecsup.devtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa la tabla "tareas" en Room, relacionada con un proyecto mediante proyectoId.
 */
@Entity(tableName = "tareas")
data class TareaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String, // COMENTARIO PARA SUSTENTACIÓN: Garantiza privacidad de tareas por UID

    val proyectoId: Int,

    val nombre: String,

    val descripcion: String,

    val estado: String,

    val prioridad: String = "Baja"
)
