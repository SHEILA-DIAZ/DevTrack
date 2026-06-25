package com.tecsup.devtrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Clase principal de la base de datos de Room que centraliza las entidades y los DAOs.
 */
@Database(
    entities = [
        ProyectoEntity::class,
        TareaEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ProyectoDatabase : RoomDatabase() {

    abstract fun proyectoDao(): ProyectoDao

    abstract fun tareaDao(): TareaDao
}