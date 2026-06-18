package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.devtrack.repository.ProyectoRepository

class ProyectoViewModelFactory(
    private val repository: ProyectoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProyectoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProyectoViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}