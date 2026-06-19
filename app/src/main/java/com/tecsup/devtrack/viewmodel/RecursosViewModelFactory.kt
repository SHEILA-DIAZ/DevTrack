package com.tecsup.devtrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.devtrack.repository.RecursosRepository

/**
 * Factoría para instanciar el RecursosViewModel con su repositorio.
 */
class RecursosViewModelFactory(
    private val repository: RecursosRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecursosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecursosViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
