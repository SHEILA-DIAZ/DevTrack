package com.tecsup.devtrack.navigation

/**
 * Define las rutas de navegación de la aplicación y ayuda a construir URIs con parámetros.
 */
object Routes {

    const val DASHBOARD = "dashboard"

    const val PROYECTOS = "proyectos"

    const val TAREAS = "tareas/{proyectoId}"

    fun tareas(proyectoId: Int): String {
        return "tareas/$proyectoId"
    }
}