package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProyectoViewModel : ViewModel() {

    private val repository = ProyectoRepository()

    private var proyectoEditando: Proyecto? = null

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

        if (proyectoEditando != null) {
            actualizarProyecto()
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

    fun seleccionarProyecto(proyecto: Proyecto) {
        proyectoEditando = proyecto

        _uiState.update {
            it.copy(
                nombre = proyecto.nombre,
                descripcion = proyecto.descripcion
            )
        }
    }

    private fun actualizarProyecto() {
        val proyecto = proyectoEditando ?: return

        val proyectoActualizado = proyecto.copy(
            nombre = _uiState.value.nombre,
            descripcion = _uiState.value.descripcion
        )

        repository.actualizarProyecto(proyectoActualizado)

        _uiState.update {
            it.copy(
                proyectos = repository.obtenerProyectos(),
                nombre = "",
                descripcion = ""
            )
        }

        proyectoEditando = null
    }

    fun eliminarProyecto(proyecto: Proyecto) {
        repository.eliminarProyecto(proyecto)

        _uiState.update {
            it.copy(
                proyectos = repository.obtenerProyectos()
            )
        }
    }
}