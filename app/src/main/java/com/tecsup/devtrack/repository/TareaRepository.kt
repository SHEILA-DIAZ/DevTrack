package com.tecsup.devtrack.repository

import com.tecsup.devtrack.data.local.TareaDao
import com.tecsup.devtrack.data.local.toEntity
import com.tecsup.devtrack.data.local.toTarea
import com.tecsup.devtrack.model.Tarea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio para la gestión de tareas, desacoplando la lógica de datos de la UI.
 */
class TareaRepository(
    private val tareaDao: TareaDao
) {

    fun obtenerTareas(): Flow<List<Tarea>> {
        return tareaDao.obtenerTareas().map { lista ->
            lista.map { entity ->
                entity.toTarea()
            }
        }
    }

    fun obtenerTareasPorProyecto(proyectoId: Int): Flow<List<Tarea>> {
        return tareaDao.obtenerTareasPorProyecto(proyectoId).map { lista ->
            lista.map { entity ->
                entity.toTarea()
            }
        }
    }

    suspend fun guardarTarea(tarea: Tarea) {
        tareaDao.guardarTarea(tarea.toEntity())
    }

    suspend fun actualizarTarea(tarea: Tarea) {
        tareaDao.actualizarTarea(tarea.toEntity())
    }

    suspend fun eliminarTarea(tarea: Tarea) {
        tareaDao.eliminarTarea(tarea.toEntity())
    }
}