package com.tecsup.devtrack.data.remote

import retrofit2.http.GET

/**
 * Interfaz que define los endpoints de la API que Retrofit consumirá.
 */
interface RecursoApiService {

    @GET("random")
    suspend fun obtenerFraseMotivacional(): List<RecursoResponse>
}
