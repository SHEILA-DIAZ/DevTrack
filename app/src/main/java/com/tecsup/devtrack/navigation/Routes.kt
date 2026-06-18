package com.tecsup.devtrack.navigation

object Routes {

    const val DASHBOARD = "dashboard"

    const val PROYECTOS = "proyectos"

    const val TAREAS = "tareas/{proyectoId}"

    fun tareas(proyectoId: Int): String {
        return "tareas/$proyectoId"
    }
}