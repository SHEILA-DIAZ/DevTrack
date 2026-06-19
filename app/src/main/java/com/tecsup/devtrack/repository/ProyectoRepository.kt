package com.tecsup.devtrack.repository

import com.tecsup.devtrack.data.local.ProyectoDao
import com.tecsup.devtrack.data.local.toEntity
import com.tecsup.devtrack.data.local.toProyecto
import com.tecsup.devtrack.model.Proyecto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio que sirve de intermediario entre los DAOs y los ViewModels.
 * Gestiona el acceso a los datos de proyectos.
 */
class ProyectoRepository(
    private val proyectoDao: ProyectoDao
) {

    fun obtenerProyectos(): Flow<List<Proyecto>> {
        return proyectoDao.obtenerProyectos().map { lista ->
            lista.map { entity ->
                entity.toProyecto()
            }
        }
    }

    suspend fun guardarProyecto(proyecto: Proyecto) {
        proyectoDao.guardarProyecto(proyecto.toEntity())
    }

    suspend fun actualizarProyecto(proyecto: Proyecto) {
        proyectoDao.actualizarProyecto(proyecto.toEntity())
    }

    suspend fun eliminarProyecto(proyecto: Proyecto) {
        proyectoDao.eliminarProyecto(proyecto.toEntity())
    }
}