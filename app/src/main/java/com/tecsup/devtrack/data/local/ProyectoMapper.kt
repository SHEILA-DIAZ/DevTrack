package com.tecsup.devtrack.data.local

import com.tecsup.devtrack.model.Proyecto

fun ProyectoEntity.toProyecto(): Proyecto {
    return Proyecto(
        id = id,
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
        nombre = nombre,
        descripcion = descripcion,
        tecnologias = tecnologias,
        estado = estado,
        fechaInicio = fechaInicio,
        fechaLimite = fechaLimite,
        observaciones = observaciones
    )
}