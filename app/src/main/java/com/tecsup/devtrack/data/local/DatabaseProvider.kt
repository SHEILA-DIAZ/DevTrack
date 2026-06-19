package com.tecsup.devtrack.data.local

import android.content.Context
import androidx.room.Room

/**
 * Proveedor de la base de datos que asegura una única instancia (Singleton) de la base de datos local.
 */
object DatabaseProvider {

    @Volatile
    private var database: ProyectoDatabase? = null

    fun getDatabase(context: Context): ProyectoDatabase {
        return database ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                ProyectoDatabase::class.java,
                "devtrack_database"
            )
                .fallbackToDestructiveMigration(true)
                .build()

            database = instance
            instance
        }
    }
}