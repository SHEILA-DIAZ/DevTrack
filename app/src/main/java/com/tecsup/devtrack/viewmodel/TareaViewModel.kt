package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.repository.TareaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que conecta la UI de tareas con el repositorio.
 */
class TareaViewModel(
    private val repository: TareaRepository
) : ViewModel() {

    private var tareaEditando: Tarea? = null
    private var dataJob: kotlinx.coroutines.Job? = null
    private var currentUid: String = ""

    private val _uiState = MutableStateFlow(TareaUiState())
    val uiState: StateFlow<TareaUiState> = _uiState

    /**
     * Carga las tareas vinculadas al usuario autenticado.
     */
    fun cargarDatosUsuario(userId: String) {
        if (userId.isEmpty()) return
        currentUid = userId
        
        dataJob?.cancel()
        dataJob = viewModelScope.launch {
            repository.sincronizarDesdeNube()
            repository.obtenerTareas(userId).collect { tareas ->
                _uiState.update { it.copy(tareas = tareas) }
            }
        }
    }

    /**
     * Resetea el estado al cerrar sesión.
     */
    fun limpiarDatos() {
        dataJob?.cancel()
        currentUid = ""
        _uiState.update { TareaUiState() }
        tareaEditando = null
    }

    /**
     * Carga todas las tareas del usuario actual (Vista general).
     */
    fun cargarTodasLasTareas() {
        if (currentUid.isEmpty()) return
        
        dataJob?.cancel()
        dataJob = viewModelScope.launch {
            repository.obtenerTareas(currentUid).collect { tareas ->
                _uiState.update { it.copy(tareas = tareas, proyectoId = 0) }
            }
        }
    }

    /**
     * Filtra tareas por proyecto específico del usuario actual.
     */
    fun cargarTareasPorProyecto(proyectoId: Int) {
        _uiState.update { it.copy(proyectoId = proyectoId) }
        if (currentUid.isEmpty()) return

        dataJob?.cancel()
        dataJob = viewModelScope.launch {
            repository.obtenerTareasPorProyecto(proyectoId, currentUid).collect { tareas ->
                _uiState.update { it.copy(tareas = tareas) }
            }
        }
    }

    fun actualizarNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, mensajeError = "") }
    }

    fun actualizarDescripcion(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion, mensajeError = "") }
    }

    fun actualizarEstado(estado: String) {
        _uiState.update { it.copy(estado = estado) }
    }

    fun actualizarPrioridad(prioridad: String) {
        _uiState.update { it.copy(prioridad = prioridad) }
    }

    fun guardarTarea() {
        val estadoActual = _uiState.value

        if (estadoActual.nombre.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Ingrese el nombre de la tarea") }
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
                estado = estadoActual.estado,
                prioridad = estadoActual.prioridad
            )

            repository.guardarTarea(nuevaTarea)
            limpiarFormulario()
        }
    }

    fun seleccionarTarea(tarea: Tarea) {
        tareaEditando = tarea
        _uiState.update {
            it.copy(
                nombre = tarea.nombre,
                descripcion = tarea.descripcion,
                estado = tarea.estado,
                prioridad = tarea.prioridad,
                mensajeError = "",
                isEditing = true
            )
        }
    }

    private suspend fun actualizarTarea() {
        val tarea = tareaEditando ?: return
        val estadoActual = _uiState.value

        val tareaActualizada = tarea.copy(
            nombre = estadoActual.nombre,
            descripcion = estadoActual.descripcion,
            estado = estadoActual.estado,
            prioridad = estadoActual.prioridad
        )

        repository.actualizarTarea(tareaActualizada)
        limpiarFormulario()
    }

    fun eliminarTarea(tarea: Tarea) {
        viewModelScope.launch {
            repository.eliminarTarea(tarea)
        }
    }

    fun limpiarFormulario() {
        tareaEditando = null
        _uiState.update {
            it.copy(
                nombre = "",
                descripcion = "",
                estado = "Pendiente",
                prioridad = "Baja",
                mensajeError = "",
                isEditing = false
            )
        }
    }
}
