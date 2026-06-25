package com.tecsup.devtrack.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz DAO que define las operaciones CRUD para la tabla de proyectos.
 */
@Dao
interface ProyectoDao {

    @Query("SELECT * FROM proyectos WHERE userId = :userId")
    fun obtenerProyectosPorUsuario(userId: String): Flow<List<ProyectoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarProyecto(proyecto: ProyectoEntity): Long

    @Update
    suspend fun actualizarProyecto(proyecto: ProyectoEntity)

    @Delete
    suspend fun eliminarProyecto(proyecto: ProyectoEntity)
    
    @Query("DELETE FROM proyectos WHERE userId = :userId")
    suspend fun limpiarCacheUsuario(userId: String)
}
