package com.tecsup.devtrack.data.local

import com.tecsup.devtrack.model.Tarea

/**
 * Mappers para convertir datos de tareas entre la base de datos y la interfaz de usuario.
 */
fun TareaEntity.toTarea(): Tarea {
    return Tarea(
        id = id,
        userId = userId,
        proyectoId = proyectoId,
        nombre = nombre,
        descripcion = descripcion,
        estado = estado
    )
}

fun Tarea.toEntity(): TareaEntity {
    return TareaEntity(
        id = id,
        userId = userId,
        proyectoId = proyectoId,
        nombre = nombre,
        descripcion = descripcion,
        estado = estado
    )
}
