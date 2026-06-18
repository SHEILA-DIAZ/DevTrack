package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProyectoViewModel(
    private val repository: ProyectoRepository
) : ViewModel() {

    private var proyectoEditando: Proyecto? = null

    private val _uiState = MutableStateFlow(ProyectoUiState())
    val uiState: StateFlow<ProyectoUiState> = _uiState

    init {
        cargarProyectos()
    }

    private fun cargarProyectos() {
        viewModelScope.launch {
            repository.obtenerProyectos().collect { proyectos ->
                _uiState.update {
                    it.copy(proyectos = proyectos)
                }
            }
        }
    }

    fun actualizarNombre(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre, mensajeError = "")
        }
    }

    fun actualizarDescripcion(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion, mensajeError = "")
        }
    }

    fun actualizarEstado(estado: String) {
        _uiState.update {
            it.copy(estado = estado)
        }
    }

    fun guardarProyecto() {
        val estadoActual = _uiState.value

        if (estadoActual.nombre.isBlank() || estadoActual.descripcion.isBlank()) {
            _uiState.update {
                it.copy(mensajeError = "Complete todos los campos")
            }
            return
        }

        viewModelScope.launch {
            if (proyectoEditando != null) {
                actualizarProyecto()
                return@launch
            }

            val nuevoProyecto = Proyecto(
                nombre = estadoActual.nombre,
                descripcion = estadoActual.descripcion,
                tecnologias = "Kotlin",
                estado = estadoActual.estado,
                fechaInicio = "2026-01-01",
                fechaLimite = "2026-12-31",
                observaciones = "Proyecto registrado en DevTrack"
            )

            repository.guardarProyecto(nuevoProyecto)

            _uiState.update {
                it.copy(
                    nombre = "",
                    descripcion = "",
                    estado = "Planificado",
                    mensajeError = ""
                )
            }
        }
    }

    fun seleccionarProyecto(proyecto: Proyecto) {
        proyectoEditando = proyecto

        _uiState.update {
            it.copy(
                nombre = proyecto.nombre,
                descripcion = proyecto.descripcion,
                estado = proyecto.estado,
                mensajeError = ""
            )
        }
    }

    private suspend fun actualizarProyecto() {
        val proyecto = proyectoEditando ?: return

        val proyectoActualizado = proyecto.copy(
            nombre = _uiState.value.nombre,
            descripcion = _uiState.value.descripcion,
            estado = _uiState.value.estado
        )

        repository.actualizarProyecto(proyectoActualizado)

        _uiState.update {
            it.copy(
                nombre = "",
                descripcion = "",
                estado = "Planificado",
                mensajeError = ""
            )
        }

        proyectoEditando = null
    }

    fun eliminarProyecto(proyecto: Proyecto) {
        viewModelScope.launch {
            repository.eliminarProyecto(proyecto)
        }
    }
}