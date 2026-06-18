package com.tecsup.devtrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProyectoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProyectoDatabase : RoomDatabase() {

    abstract fun proyectoDao(): ProyectoDao
}