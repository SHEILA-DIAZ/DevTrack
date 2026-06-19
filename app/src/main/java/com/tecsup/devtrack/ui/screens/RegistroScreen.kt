package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de Registro de DevTrack rediseñada con estilo Premium.
 * COMENTARIO PARA SUSTENTACIÓN: Implementa una interfaz de usuario moderna tipo SaaS,
 * con validaciones locales exhaustivas y una jerarquía visual clara.
 */
@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onNavegarAlLogin: () -> Unit
) {
    // Estados para los campos del formulario (Mantenidos para lógica)
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Estados para visibilidad de contraseña
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Estados para mensajes de error (Mantenidos para lógica)
    var nombreError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    var registroExitoso by remember { mutableStateOf(false) }

    // COMENTARIO PARA SUSTENTACIÓN: Fondo con formas decorativas sutiles para estética premium.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Decoraciones de fondo
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-150).dp, y = (-100).dp)
                .background(Color(0xFF4B6CB7).copy(alpha = 0.05f), CircleShape)
                .blur(50.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Encabezado con identidad de marca y estilo moderno.
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF4B6CB7),
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("DT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1C1E)
            )

            Text(
                text = "Únete a DevTrack y comienza a gestionar tus proyectos.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Contenedor tipo tarjeta para el formulario de registro.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo: Nombre Completo
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; nombreError = null },
                        label = { Text("Nombre completo") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = nombreError != null,
                        supportingText = { nombreError?.let { Text(it) } }
                    )

                    // Campo: Correo Electrónico
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it; correoError = null },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = correoError != null,
                        supportingText = { correoError?.let { Text(it) } }
                    )

                    // Campo: Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = null },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Info else Icons.Default.Info, // Fallback por set básico
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError != null,
                        supportingText = { passwordError?.let { Text(it) } }
                    )

                    // Campo: Confirmar Contraseña
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; confirmPasswordError = null },
                        label = { Text("Confirmar contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Info else Icons.Default.Info, // Fallback por set básico
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = confirmPasswordError != null,
                        supportingText = { confirmPasswordError?.let { Text(it) } }
                    )

                    // Tarjeta informativa de seguridad
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF4B6CB7).copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4B6CB7), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Tu información está segura", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF4B6CB7))
                                Text("Usamos buenas prácticas para proteger tus datos.", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    }

                    if (registroExitoso) {
                        Text(
                            text = "Cuenta creada correctamente",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // COMENTARIO PARA SUSTENTACIÓN: Botón principal con estilo moderno.
                    Button(
                        onClick = {
                            var esValido = true
                            if (nombre.length < 3) { nombreError = "Mínimo 3 caracteres"; esValido = false }
                            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                            if (!correo.matches(emailPattern.toRegex())) { correoError = "Correo inválido"; esValido = false }
                            if (password.length < 6) { passwordError = "Mínimo 6 caracteres"; esValido = false }
                            if (password != confirmPassword) { confirmPasswordError = "No coinciden"; esValido = false }

                            if (esValido) { registroExitoso = true }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("Crear cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onNavegarAlLogin) {
                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    color = Color(0xFF4B6CB7),
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(onClick = onVolver) {
                Text("Volver al inicio", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Al registrarte aceptas nuestros términos y condiciones.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
