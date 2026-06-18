package com.tecsup.devtrack.data.local

import com.tecsup.devtrack.model.Tarea

fun TareaEntity.toTarea(): Tarea {
    return Tarea(
        id = id,
        proyectoId = proyectoId,
        nombre = nombre,
        descripcion = descripcion,
        estado = estado
    )
}

fun Tarea.toEntity(): TareaEntity {
    return TareaEntity(
        id = id,
        proyectoId = proyectoId,
        nombre = nombre,
        descripcion = descripcion,
        estado = estado
    )
}