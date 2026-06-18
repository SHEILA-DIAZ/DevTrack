package com.tecsup.devtrack.repository

import com.tecsup.devtrack.model.Proyecto

class ProyectoRepository {

    private val proyectos = mutableListOf<Proyecto>()

    fun obtenerProyectos(): List<Proyecto> {
        return proyectos
    }

    fun guardarProyecto(proyecto: Proyecto) {
        proyectos.add(proyecto)
    }

    fun eliminarProyecto(proyecto: Proyecto) {
        proyectos.remove(proyecto)
    }

    fun actualizarProyecto(proyectoActualizado: Proyecto) {
        val index = proyectos.indexOfFirst { it.id == proyectoActualizado.id }

        if (index != -1) {
            proyectos[index] = proyectoActualizado
        }
    }
}