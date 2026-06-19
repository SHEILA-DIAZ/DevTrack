package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Pantalla de Registro de DevTrack.
 * Implementa validaciones locales para asegurar la calidad de los datos.
 * COMENTARIO PARA SUSTENTACIÓN: Esta pantalla simula el proceso de creación de cuenta.
 * En la Parte 2 se conectará con Firebase Auth para persistir usuarios reales.
 */
@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onNavegarAlLogin: () -> Unit
) {
    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Estados para mensajes de error
    var nombreError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    var registroExitoso by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Regístrate para comenzar a gestionar tus proyectos.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo: Nombre Completo
        OutlinedTextField(
            value = nombre,
            onValueChange = { 
                nombre = it
                nombreError = null 
            },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = nombreError != null,
            supportingText = { nombreError?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Correo Electrónico
        OutlinedTextField(
            value = correo,
            onValueChange = { 
                correo = it
                correoError = null 
            },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = correoError != null,
            supportingText = { correoError?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                passwordError = null 
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Confirmar Contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it
                confirmPasswordError = null 
            },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError != null,
            supportingText = { confirmPasswordError?.let { Text(it) } }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (registroExitoso) {
            Text(
                text = "Cuenta creada correctamente",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Botón Registrarse con validación lógica
        Button(
            onClick = {
                // COMENTARIO PARA SUSTENTACIÓN: Lógica de validación de campos
                var esValido = true

                if (nombre.length < 3) {
                    nombreError = "El nombre debe tener mínimo 3 caracteres"
                    esValido = false
                }

                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (!correo.matches(emailPattern.toRegex())) {
                    correoError = "Ingrese un correo válido (ej: usuario@correo.com)"
                    esValido = false
                }

                if (password.length < 6) {
                    passwordError = "La contraseña debe tener mínimo 6 caracteres"
                    esValido = false
                }

                if (password != confirmPassword) {
                    confirmPasswordError = "Las contraseñas no coinciden"
                    esValido = false
                }

                if (esValido) {
                    // Simulación de registro exitoso
                    registroExitoso = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavegarAlLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }

        TextButton(onClick = onVolver) {
            Text("Volver al inicio")
        }
    }
}
