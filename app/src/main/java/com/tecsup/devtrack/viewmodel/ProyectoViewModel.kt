package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de negocio de proyectos.
 */
class ProyectoViewModel(
    private val repository: ProyectoRepository
) : ViewModel() {

    private var proyectoEditando: Proyecto? = null
    private var dataJob: kotlinx.coroutines.Job? = null

    private val _uiState = MutableStateFlow(ProyectoUiState())
    val uiState: StateFlow<ProyectoUiState> = _uiState

    /**
     * Inicia la observación de datos para un usuario específico.
     * COMENTARIO PARA SUSTENTACIÓN: Garantiza el aislamiento multiusuario reiniciando el Flow.
     */
    fun cargarDatosUsuario(userId: String) {
        if (userId.isEmpty()) return
        
        dataJob?.cancel()
        dataJob = viewModelScope.launch {
            repository.sincronizarDesdeNube()
            repository.obtenerProyectos(userId).collect { proyectos ->
                _uiState.update { it.copy(proyectos = proyectos) }
            }
        }
    }

    /**
     * Limpia los datos en memoria al cerrar sesión.
     */
    fun limpiarDatos() {
        dataJob?.cancel()
        _uiState.update { ProyectoUiState() }
        proyectoEditando = null
    }

    /**
     * Limpia el formulario para registrar un nuevo proyecto.
     */
    fun limpiarFormulario() {
        proyectoEditando = null
        _uiState.update {
            it.copy(
                nombre = "",
                descripcion = "",
                tecnologias = "",
                fechaInicio = "",
                fechaLimite = "",
                estado = "Planificado",
                mensajeError = "",
                mensajeExito = ""
            )
        }
    }

    fun actualizarNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, mensajeError = "", mensajeExito = "") }
    }

    fun actualizarDescripcion(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion, mensajeError = "", mensajeExito = "") }
    }

    fun actualizarTecnologias(tecnologias: String) {
        _uiState.update { it.copy(tecnologias = tecnologias, mensajeError = "", mensajeExito = "") }
    }

    fun actualizarFechaInicio(fecha: String) {
        _uiState.update { it.copy(fechaInicio = fecha, mensajeError = "", mensajeExito = "") }
    }

    fun actualizarFechaLimite(fecha: String) {
        _uiState.update { it.copy(fechaLimite = fecha, mensajeError = "", mensajeExito = "") }
    }

    fun actualizarEstado(estado: String) {
        _uiState.update { it.copy(estado = estado, mensajeError = "", mensajeExito = "") }
    }

    fun guardarProyecto() {
        val estadoActual = _uiState.value

        // COMENTARIO PARA SUSTENTACIÓN: Validaciones de negocio antes de la persistencia.
        if (estadoActual.nombre.length < 3) {
            _uiState.update { it.copy(mensajeError = "El nombre debe tener mínimo 3 caracteres") }
            return
        }
        if (estadoActual.descripcion.length < 5) {
            _uiState.update { it.copy(mensajeError = "La descripción debe tener mínimo 5 caracteres") }
            return
        }
        if (estadoActual.tecnologias.isBlank() || estadoActual.fechaInicio.isBlank() || estadoActual.fechaLimite.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Complete todos los campos obligatorios") }
            return
        }

        // Validación de Fechas
        try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            sdf.isLenient = false
            val inicio = sdf.parse(estadoActual.fechaInicio)
            val limite = sdf.parse(estadoActual.fechaLimite)
            if (limite != null && inicio != null && limite.before(inicio)) {
                _uiState.update { it.copy(mensajeError = "La fecha límite no puede ser anterior a la fecha de inicio.") }
                return
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(mensajeError = "Ingresa una fecha válida con formato dd/MM/yyyy.") }
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
                tecnologias = estadoActual.tecnologias,
                estado = estadoActual.estado,
                fechaInicio = estadoActual.fechaInicio,
                fechaLimite = estadoActual.fechaLimite,
                observaciones = "Proyecto registrado en DevTrack"
            )

            repository.guardarProyecto(nuevoProyecto)

            // COMENTARIO PARA SUSTENTACIÓN: Limpieza del formulario y mensaje de éxito.
            _uiState.update {
                it.copy(
                    nombre = "",
                    descripcion = "",
                    tecnologias = "",
                    fechaInicio = "",
                    fechaLimite = "",
                    estado = "Planificado",
                    mensajeError = "",
                    mensajeExito = "Proyecto registrado correctamente"
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
                tecnologias = proyecto.tecnologias,
                fechaInicio = proyecto.fechaInicio,
                fechaLimite = proyecto.fechaLimite,
                estado = proyecto.estado,
                mensajeError = "",
                mensajeExito = ""
            )
        }
    }

    private suspend fun actualizarProyecto() {
        val proyecto = proyectoEditando ?: return
        val estadoActual = _uiState.value

        val proyectoActualizado = proyecto.copy(
            nombre = estadoActual.nombre,
            descripcion = estadoActual.descripcion,
            tecnologias = estadoActual.tecnologias,
            fechaInicio = estadoActual.fechaInicio,
            fechaLimite = estadoActual.fechaLimite,
            estado = estadoActual.estado
        )

        repository.actualizarProyecto(proyectoActualizado)

        _uiState.update {
            it.copy(
                nombre = "",
                descripcion = "",
                tecnologias = "",
                fechaInicio = "",
                fechaLimite = "",
                estado = "Planificado",
                mensajeError = "",
                mensajeExito = "Proyecto actualizado correctamente"
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
