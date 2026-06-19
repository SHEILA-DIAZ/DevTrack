package com.tecsup.devtrack.viewmodel

/**
 * Representa los diferentes estados de la pantalla de Recursos Tecnológicos.
 */
data class RecursosUiState(
    val isLoading: Boolean = false,
    val frase: String = "",
    val autor: String = "",
    val error: String? = null
)
