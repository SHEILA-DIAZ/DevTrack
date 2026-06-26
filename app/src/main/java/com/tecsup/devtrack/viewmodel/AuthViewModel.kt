package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.devtrack.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de autenticación y comunica el estado a la UI.
 */
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            viewModelScope.launch {
                val profile = authRepository.getUserProfile(currentUser.uid)
                _uiState.update {
                    it.copy(
                        isAuthenticated = true,
                        userEmail = currentUser.email,
                        userName = profile?.get("nombreCompleto") as? String,
                        userId = currentUser.uid
                    )
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = authRepository.login(email, password)
                if (user != null) {
                    val profile = authRepository.getUserProfile(user.uid)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            userEmail = user.email,
                            userName = profile?.get("nombreCompleto") as? String,
                            userId = user.uid
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = mapFirebaseError(e)) }
            }
        }
    }

    fun register(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = authRepository.register(nombre, email, password)
                if (user != null) {
                    // COMENTARIO PARA SUSTENTACIÓN: Se cierra la sesión automática para obligar al login manual
                    authRepository.logout()
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = false,
                            userEmail = null,
                            userName = null,
                            userId = null,
                            successMessage = "Cuenta creada correctamente. Por favor, inicia sesión."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = mapFirebaseError(e)) }
            }
        }
    }

    private fun mapFirebaseError(e: Exception): String {
        val message = e.message ?: ""
        return when {
            message.contains("network-request-failed") -> "No se pudo conectar. Verifica tu conexión a Internet."
            message.contains("user-not-found") -> "No existe una cuenta registrada con este correo."
            message.contains("wrong-password") -> "La contraseña ingresada es incorrecta."
            message.contains("invalid-credential") -> "Correo o contraseña incorrectos."
            message.contains("email-already-in-use") -> "Este correo ya está registrado."
            message.contains("invalid-email") -> "Ingresa un correo electrónico válido."
            else -> "Ocurrió un error. Inténtalo nuevamente."
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.update {
            it.copy(
                isAuthenticated = false,
                userEmail = null,
                userName = null,
                userId = null,
                successMessage = null
            )
        }
    }
    
    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
