package com.tecsup.devtrack.data.local

import com.tecsup.devtrack.model.Proyecto

/**
 * Funciones de extensión para convertir entre ProyectoEntity (DB) y Proyecto (UI).
 * Esto separa los datos de la capa de persistencia de los modelos de negocio.
 */
fun ProyectoEntity.toProyecto(): Proyecto {
    return Proyecto(
        id = id,
        userId = userId,
        nombre = nombre,
        descripcion = descripcion,
        tecnologias = tecnologias,
        estado = estado,
        fechaInicio = fechaInicio,
        fechaLimite = fechaLimite,
        observaciones = observaciones
    )
}

fun Proyecto.toEntity(): ProyectoEntity {
    return ProyectoEntity(
        id = id,
        userId = userId,
        nombre = nombre,
        descripcion = descripcion,
        tecnologias = tecnologias,
        estado = estado,
        fechaInicio = fechaInicio,
        fechaLimite = fechaLimite,
        observaciones = observaciones
    )
}
