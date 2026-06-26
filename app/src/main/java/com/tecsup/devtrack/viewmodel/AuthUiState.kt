package com.tecsup.devtrack.viewmodel

/**
 * Representa el estado de la autenticación en la interfaz de usuario.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val userEmail: String? = null,
    val userName: String? = null,
    val userId: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
