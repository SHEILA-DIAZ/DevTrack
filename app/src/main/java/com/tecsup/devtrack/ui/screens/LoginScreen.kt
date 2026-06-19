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
 * Pantalla de Inicio de Sesión de DevTrack.
 * COMENTARIO PARA SUSTENTACIÓN: Esta pantalla gestiona el acceso de usuarios.
 * Implementa validaciones de formato antes de permitir la navegación.
 * En la Parte 2 se integrará con Firebase Authentication para el login real.
 */
@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onNavegarAlRegistro: () -> Unit,
    onIngresarAlDashboard: () -> Unit
) {
    // Estados para los campos de texto
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var recordarme by remember { mutableStateOf(false) }

    // Estados para el manejo de errores
    var correoError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Accede a DevTrack para continuar gestionando tus proyectos.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = recordarme,
                    onCheckedChange = { recordarme = it }
                )
                Text(text = "Recordarme", style = MaterialTheme.typography.bodySmall)
            }
            
            TextButton(onClick = { /* Pendiente Entrega 2 */ }) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Ingresar con lógica de validación
        Button(
            onClick = {
                // COMENTARIO PARA SUSTENTACIÓN: Validaciones obligatorias de negocio
                var esValido = true

                // Validación de correo
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (correo.isBlank()) {
                    correoError = "El correo no puede estar vacío"
                    esValido = false
                } else if (!correo.matches(emailPattern.toRegex())) {
                    correoError = "Formato de correo inválido"
                    esValido = false
                }

                // Validación de contraseña
                if (password.isBlank()) {
                    passwordError = "La contraseña no puede estar vacía"
                    esValido = false
                } else if (password.length < 6) {
                    passwordError = "Mínimo 6 caracteres"
                    esValido = false
                }

                if (esValido) {
                    // Simulación de inicio de sesión y paso al Dashboard
                    onIngresarAlDashboard()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavegarAlRegistro) {
            Text("¿No tienes cuenta? Regístrate")
        }

        TextButton(onClick = onVolver) {
            Text("Volver al inicio")
        }
    }
}
