package com.tecsup.devtrack.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repositorio encargado de la gestión de autenticación con Firebase y perfil de usuario.
 */
class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun login(email: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user
    }

    suspend fun register(nombre: String, email: String, password: String): FirebaseUser? {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user
        
        if (user != null) {
            // Guardar perfil en Firestore
            val profile = mapOf(
                "nombreCompleto" to nombre,
                "correo" to email,
                "uid" to user.uid
            )
            firestore.collection("usuarios").document(user.uid).set(profile).await()
        }
        
        return user
    }

    suspend fun getUserProfile(uid: String): Map<String, Any>? {
        return try {
            val doc = firestore.collection("usuarios").document(uid).get().await()
            doc.data
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}
