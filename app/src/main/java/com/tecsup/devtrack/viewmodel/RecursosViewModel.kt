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

    // Lista de imágenes relacionadas con tecnología e innovación
    private val imagenesMotivacionales = listOf(
        "https://images.unsplash.com/photo-1517694712202-14dd9538aa97", // Laptop/Code
        "https://images.unsplash.com/photo-1519389950473-47ba0277781c", // Team/Tech
        "https://images.unsplash.com/photo-1550751827-4bd374c3f58b", // Cyberpunk/Tech
        "https://images.unsplash.com/photo-1498050108023-c5249f4df085", // Developer/Desk
        "https://images.unsplash.com/photo-1522071823991-b96c0d3ec6c2", // Innovation/Abstract
        "https://images.unsplash.com/photo-1484417894907-623942c8ee29", // Programming/Modern
        "https://images.unsplash.com/photo-1451187580459-43490279c0fa"  // Connection/Network
    )

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
                // Seleccionamos una imagen aleatoria de la lista
                val nuevaImagen = imagenesMotivacionales.random()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        frase = resultado.frase,
                        autor = resultado.autor,
                        imageUrl = nuevaImagen
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
