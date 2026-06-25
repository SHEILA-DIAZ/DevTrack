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
            _uiState.update {
                it.copy(
                    isAuthenticated = true,
                    userEmail = currentUser.email,
                    userId = currentUser.uid
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = authRepository.login(email, password)
                if (user != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            userEmail = user.email,
                            userId = user.uid
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = authRepository.register(email, password)
                if (user != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true, // Podría requerir login después, pero solemos loguear al registrar
                            userEmail = user.email,
                            userId = user.uid,
                            successMessage = "Cuenta creada correctamente"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.update {
            it.copy(
                isAuthenticated = false,
                userEmail = null,
                userId = null,
                successMessage = null
            )
        }
    }
    
    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
