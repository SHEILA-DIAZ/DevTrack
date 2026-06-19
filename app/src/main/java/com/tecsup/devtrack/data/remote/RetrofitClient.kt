package com.tecsup.devtrack.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente de Retrofit configurado como Singleton para gestionar la conexión con la API.
 */
object RetrofitClient {

    private const val BASE_URL = "https://zenquotes.io/api/"

    val instance: RecursoApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RecursoApiService::class.java)
    }
}
