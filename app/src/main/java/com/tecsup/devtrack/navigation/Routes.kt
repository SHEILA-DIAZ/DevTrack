package com.tecsup.devtrack.navigation

/**
 * Define las rutas de navegación de la aplicación y ayuda a construir URIs con parámetros.
 */
object Routes {

    const val SPLASH = "splash"

    const val LOGIN = "login"

    const val REGISTRO = "registro"

    const val DASHBOARD = "dashboard"

    const val PROYECTOS = "proyectos"

    const val RECURSOS = "recursos"

    const val PERFIL = "perfil"

    const val DETALLE_PROYECTO = "detalle_proyecto"

    const val TAREAS = "tareas/{proyectoId}"

    fun tareas(proyectoId: Int): String {
        return "tareas/$proyectoId"
    }
}