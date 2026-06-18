package com.tecsup.devtrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProyectoEntity::class,
        TareaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ProyectoDatabase : RoomDatabase() {

    abstract fun proyectoDao(): ProyectoDao

    abstract fun tareaDao(): TareaDao
}