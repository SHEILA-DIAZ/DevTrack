package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.repository.TareaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TareaViewModel(
    private val repository: TareaRepository
) : ViewModel() {

    private var tareaEditando: Tarea? = null

    private val _uiState = MutableStateFlow(TareaUiState())
    val uiState: StateFlow<TareaUiState> = _uiState

    init {
        cargarTodasLasTareas()
    }

    private fun cargarTodasLasTareas() {
        viewModelScope.launch {
            repository.obtenerTareas().collect { tareas ->
                _uiState.update {
                    it.copy(tareas = tareas)
                }
            }
        }
    }

    fun cargarTareasPorProyecto(proyectoId: Int) {
        _uiState.update {
            it.copy(proyectoId = proyectoId)
        }

        viewModelScope.launch {
            repository.obtenerTareasPorProyecto(proyectoId).collect { tareas ->
                _uiState.update {
                    it.copy(tareas = tareas)
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

    fun guardarTarea() {
        val estadoActual = _uiState.value

        if (estadoActual.nombre.isBlank() || estadoActual.descripcion.isBlank()) {
            _uiState.update {
                it.copy(mensajeError = "Complete todos los campos")
            }
            return
        }

        viewModelScope.launch {
            if (tareaEditando != null) {
                actualizarTarea()
                return@launch
            }

            val nuevaTarea = Tarea(
                proyectoId = estadoActual.proyectoId,
                nombre = estadoActual.nombre,
                descripcion = estadoActual.descripcion,
                estado = estadoActual.estado
            )

            repository.guardarTarea(nuevaTarea)

            _uiState.update {
                it.copy(
                    nombre = "",
                    descripcion = "",
                    estado = "Pendiente",
                    mensajeError = ""
                )
            }
        }
    }

    fun seleccionarTarea(tarea: Tarea) {
        tareaEditando = tarea

        _uiState.update {
            it.copy(
                nombre = tarea.nombre,
                descripcion = tarea.descripcion,
                estado = tarea.estado,
                mensajeError = ""
            )
        }
    }

    private suspend fun actualizarTarea() {
        val tarea = tareaEditando ?: return

        val tareaActualizada = tarea.copy(
            nombre = _uiState.value.nombre,
            descripcion = _uiState.value.descripcion,
            estado = _uiState.value.estado
        )

        repository.actualizarTarea(tareaActualizada)

        _uiState.update {
            it.copy(
                nombre = "",
                descripcion = "",
                estado = "Pendiente",
                mensajeError = ""
            )
        }

        tareaEditando = null
    }

    fun eliminarTarea(tarea: Tarea) {
        viewModelScope.launch {
            repository.eliminarTarea(tarea)
        }
    }
}