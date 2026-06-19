package com.tecsup.devtrack.repository

import com.tecsup.devtrack.data.remote.RecursoApiService
import com.tecsup.devtrack.data.remote.RecursoResponse

/**
 * Repositorio que gestiona el consumo de recursos externos desde la API.
 */
class RecursosRepository(
    private val apiService: RecursoApiService
) {
    suspend fun obtenerFrase(): RecursoResponse? {
        return try {
            val respuesta = apiService.obtenerFraseMotivacional()
            // La API devuelve una lista, tomamos el primer elemento
            respuesta.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
}
