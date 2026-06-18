package com.tecsup.devtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")
data class TareaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val proyectoId: Int,

    val nombre: String,

    val descripcion: String,

    val estado: String
)