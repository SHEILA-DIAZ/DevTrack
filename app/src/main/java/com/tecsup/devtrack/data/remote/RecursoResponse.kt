package com.tecsup.devtrack.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Data class que representa la respuesta de la API de frases motivacionales.
 */
data class RecursoResponse(
    @SerializedName("q")
    val frase: String,
    
    @SerializedName("a")
    val autor: String
)
