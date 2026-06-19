package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.devtrack.repository.RecursosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que maneja la lógica para cargar frases motivacionales desde el repositorio.
 */
class RecursosViewModel(
    private val repository: RecursosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecursosUiState())
    val uiState: StateFlow<RecursosUiState> = _uiState

    init {
        cargarRecurso()
    }

    fun cargarRecurso() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val resultado = repository.obtenerFrase()
            if (resultado != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        frase = resultado.frase,
                        autor = resultado.autor
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudieron cargar los recursos tecnológicos"
                    )
                }
            }
        }
    }
}
