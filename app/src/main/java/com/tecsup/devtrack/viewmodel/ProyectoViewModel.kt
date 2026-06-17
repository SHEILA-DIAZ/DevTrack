package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProyectoViewModel : ViewModel() {

    private val repository = ProyectoRepository()

    private val _uiState = MutableStateFlow(ProyectoUiState())
    val uiState: StateFlow<ProyectoUiState> = _uiState

    fun actualizarNombre(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre)
        }
    }

    fun actualizarDescripcion(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    fun guardarProyecto() {
        val estadoActual = _uiState.value

        if (estadoActual.nombre.isBlank() || estadoActual.descripcion.isBlank()) {
            return
        }

        val nuevoProyecto = Proyecto(
            id = repository.obtenerProyectos().size + 1,
            nombre = estadoActual.nombre,
            descripcion = estadoActual.descripcion,
            tecnologias = "Kotlin",
            estado = "En desarrollo",
            fechaInicio = "2026-01-01",
            fechaLimite = "2026-12-31",
            observaciones = "Proyecto registrado en DevTrack"
        )

        repository.guardarProyecto(nuevoProyecto)

        _uiState.update {
            it.copy(
                proyectos = repository.obtenerProyectos(),
                nombre = "",
                descripcion = ""
            )
        }
    }
}