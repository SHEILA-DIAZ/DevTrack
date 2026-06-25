package com.tecsup.devtrack.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.devtrack.data.local.TareaDao
import com.tecsup.devtrack.data.local.toEntity
import com.tecsup.devtrack.data.local.toTarea
import com.tecsup.devtrack.model.Tarea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

/**
 * Repositorio para la gestión de tareas con sincronización en Firestore.
 * COMENTARIO PARA SUSTENTACIÓN: Cada tarea se vincula al UID del usuario para garantizar
 * que los datos sean privados y solo accesibles por su propietario.
 */
class TareaRepository(
    private val tareaDao: TareaDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val userId: String get() = auth.currentUser?.uid ?: ""

    /**
     * Lista todas las tareas del usuario desde Room.
     */
    fun obtenerTareas(userId: String): Flow<List<Tarea>> {
        return tareaDao.obtenerTareasPorUsuario(userId).map { lista ->
            lista.map { it.toTarea() }
        }
    }

    /**
     * Filtra tareas por proyecto y usuario actual.
     */
    fun obtenerTareasPorProyecto(proyectoId: Int, userId: String): Flow<List<Tarea>> {
        return tareaDao.obtenerTareasPorProyecto(proyectoId, userId).map { lista ->
            lista.map { it.toTarea() }
        }
    }

    /**
     * Guarda en Room y sincroniza con Firestore.
     */
    suspend fun guardarTarea(tarea: Tarea) {
        val tareaConUser = tarea.copy(userId = userId)
        val entity = tareaConUser.toEntity()
        
        val idGenerado = tareaDao.guardarTarea(entity)
        val tareaFinal = tareaConUser.copy(id = idGenerado.toInt())
        
        firestore.collection("usuarios")
            .document(userId)
            .collection("tareas")
            .document(idGenerado.toString())
            .set(tareaFinal)
            .await()
    }

    /**
     * Actualiza la tarea en local y remoto.
     */
    suspend fun actualizarTarea(tarea: Tarea) {
        val tareaConUser = tarea.copy(userId = userId)
        tareaDao.actualizarTarea(tareaConUser.toEntity())
        
        firestore.collection("usuarios")
            .document(userId)
            .collection("tareas")
            .document(tarea.id.toString())
            .set(tareaConUser)
            .await()
    }

    /**
     * Elimina la tarea de ambas fuentes.
     */
    suspend fun eliminarTarea(tarea: Tarea) {
        tareaDao.eliminarTarea(tarea.toEntity())
        
        firestore.collection("usuarios")
            .document(userId)
            .collection("tareas")
            .document(tarea.id.toString())
            .delete()
            .await()
    }

    /**
     * Descarga tareas desde Firestore hacia Room.
     */
    suspend fun sincronizarDesdeNube() {
        if (userId.isEmpty()) return
        
        try {
            val snapshot = firestore.collection("usuarios")
                .document(userId)
                .collection("tareas")
                .get()
                .await()
            
            val tareasRemotas = snapshot.toObjects(Tarea::class.java)
            
            tareasRemotas.forEach {
                tareaDao.guardarTarea(it.toEntity())
            }
        } catch (e: Exception) {
            // Error de red o permisos
        }
    }
}
