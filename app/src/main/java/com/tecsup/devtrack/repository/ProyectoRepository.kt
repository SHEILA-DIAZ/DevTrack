package com.tecsup.devtrack.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.devtrack.data.local.ProyectoDao
import com.tecsup.devtrack.data.local.toEntity
import com.tecsup.devtrack.data.local.toProyecto
import com.tecsup.devtrack.model.Proyecto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

/**
 * Repositorio que sincroniza proyectos entre Room (Local) y Firestore (Nube).
 * COMENTARIO PARA SUSTENTACIÓN: Implementa una estrategia Offline-first, usando Room como caché
 * y Firestore como fuente de verdad remota, filtrando siempre por el UID del usuario.
 */
class ProyectoRepository(
    private val proyectoDao: ProyectoDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val userId: String get() = auth.currentUser?.uid ?: ""

    /**
     * Obtiene proyectos desde la caché local filtrados por el usuario actual.
     */
    fun obtenerProyectos(userId: String): Flow<List<Proyecto>> {
        return proyectoDao.obtenerProyectosPorUsuario(userId).map { lista ->
            lista.map { it.toProyecto() }
        }
    }

    /**
     * Guarda un proyecto en Room y lo sincroniza con Firestore.
     */
    suspend fun guardarProyecto(proyecto: Proyecto) {
        val proyectoConUser = proyecto.copy(userId = userId)
        val entity = proyectoConUser.toEntity()
        
        // 1. Guardar en local (Room) y obtener el ID generado
        val idGenerado = proyectoDao.guardarProyecto(entity)
        val proyectoFinal = proyectoConUser.copy(id = idGenerado.toInt())
        
        // 2. Sincronizar con Firestore usando el mismo ID
        firestore.collection("usuarios")
            .document(userId)
            .collection("proyectos")
            .document(idGenerado.toString())
            .set(proyectoFinal)
            .await()
    }

    /**
     * Actualiza el proyecto en ambas fuentes de datos.
     */
    suspend fun actualizarProyecto(proyecto: Proyecto) {
        val proyectoConUser = proyecto.copy(userId = userId)
        proyectoDao.actualizarProyecto(proyectoConUser.toEntity())
        
        firestore.collection("usuarios")
            .document(userId)
            .collection("proyectos")
            .document(proyecto.id.toString())
            .set(proyectoConUser)
            .await()
    }

    /**
     * Elimina el proyecto de local y remoto.
     */
    suspend fun eliminarProyecto(proyecto: Proyecto) {
        proyectoDao.eliminarProyecto(proyecto.toEntity())
        
        firestore.collection("usuarios")
            .document(userId)
            .collection("proyectos")
            .document(proyecto.id.toString())
            .delete()
            .await()
    }

    /**
     * Sincroniza los proyectos de la nube hacia la base de datos local.
     */
    suspend fun sincronizarDesdeNube() {
        if (userId.isEmpty()) return
        
        try {
            val snapshot = firestore.collection("usuarios")
                .document(userId)
                .collection("proyectos")
                .get()
                .await()
            
            val proyectosRemotos = snapshot.toObjects(Proyecto::class.java)
            
            // Limpiar caché local y repoblar con datos de la nube
            proyectoDao.limpiarCacheUsuario(userId)
            proyectosRemotos.forEach {
                proyectoDao.guardarProyecto(it.toEntity())
            }
        } catch (e: Exception) {
            // Manejar error de sincronización
        }
    }
}
