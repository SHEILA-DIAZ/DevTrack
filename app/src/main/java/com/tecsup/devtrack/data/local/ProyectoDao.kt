package com.tecsup.devtrack.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProyectoDao {

    @Query("SELECT * FROM proyectos")
    fun obtenerProyectos(): Flow<List<ProyectoEntity>>

    @Insert
    suspend fun guardarProyecto(proyecto: ProyectoEntity)

    @Update
    suspend fun actualizarProyecto(proyecto: ProyectoEntity)

    @Delete
    suspend fun eliminarProyecto(proyecto: ProyectoEntity)
}