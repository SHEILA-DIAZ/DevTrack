package com.tecsup.devtrack.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Operaciones CRUD de Room para la gestión de tareas en la base de datos local.
 */
@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas WHERE userId = :userId")
    fun obtenerTareasPorUsuario(userId: String): Flow<List<TareaEntity>>

    @Query("SELECT * FROM tareas WHERE proyectoId = :proyectoId AND userId = :userId")
    fun obtenerTareasPorProyecto(proyectoId: Int, userId: String): Flow<List<TareaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarTarea(tarea: TareaEntity): Long

    @Update
    suspend fun actualizarTarea(tarea: TareaEntity)

    @Delete
    suspend fun eliminarTarea(tarea: TareaEntity)
}
