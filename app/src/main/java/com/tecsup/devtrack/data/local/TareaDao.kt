package com.tecsup.devtrack.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas")
    fun obtenerTareas(): Flow<List<TareaEntity>>

    @Query("SELECT * FROM tareas WHERE proyectoId = :proyectoId")
    fun obtenerTareasPorProyecto(
        proyectoId: Int
    ): Flow<List<TareaEntity>>

    @Insert
    suspend fun guardarTarea(
        tarea: TareaEntity
    )

    @Update
    suspend fun actualizarTarea(
        tarea: TareaEntity
    )

    @Delete
    suspend fun eliminarTarea(
        tarea: TareaEntity
    )
}