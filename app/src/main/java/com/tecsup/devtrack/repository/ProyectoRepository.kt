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
}