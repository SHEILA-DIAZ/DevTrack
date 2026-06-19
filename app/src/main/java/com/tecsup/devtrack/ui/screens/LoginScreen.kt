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
import coil.compose.AsyncImage

/**
 * Pantalla de Inicio de Sesión de DevTrack.
 * COMENTARIO PARA SUSTENTACIÓN: Esta pantalla gestiona el acceso de usuarios siguiendo la identidad visual
 * premium de la aplicación. Implementa validaciones y un diseño moderno tipo SaaS.
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
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados para el manejo de errores
    var correoError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Decoraciones de fondo (Consistentes con RegistroScreen)
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
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

            // COMENTARIO PARA SUSTENTACIÓN: Encabezado superior con logo unificado.
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
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1C1E)
            )

            Text(
                text = "Accede a DevTrack y continúa gestionando tus proyectos tecnológicos.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ilustración decorativa relacionada con dashboards/proyectos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=1000&auto=format&fit=crop",
                    contentDescription = "Estadísticas y Proyectos",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Tarjeta principal blanca con elevación para el formulario.
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
                    // Campo: Correo Electrónico
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { 
                            correo = it
                            correoError = null 
                        },
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
                        onValueChange = { 
                            password = it
                            passwordError = null 
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF4B6CB7)) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Info else Icons.Default.Info,
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = recordarme,
                                onCheckedChange = { recordarme = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4B6CB7))
                            )
                            Text(text = "Recordarme", style = MaterialTheme.typography.bodySmall)
                        }
                        
                        TextButton(onClick = { /* Pendiente Entrega 2 */ }) {
                            Text(
                                text = "¿Olvidaste tu contraseña?",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4B6CB7)
                            )
                        }
                    }

                    // COMENTARIO PARA SUSTENTACIÓN: Tarjeta de seguridad integrada.
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
                                Text("Acceso seguro", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF4B6CB7))
                                Text("Tus proyectos y datos están protegidos.", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    }

                    // COMENTARIO PARA SUSTENTACIÓN: Botón principal con el color identitario.
                    Button(
                        onClick = {
                            var esValido = true
                            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                            if (correo.isBlank()) {
                                correoError = "El correo no puede estar vacío"
                                esValido = false
                            } else if (!correo.matches(emailPattern.toRegex())) {
                                correoError = "Formato de correo inválido"
                                esValido = false
                            }
                            if (password.isBlank()) {
                                passwordError = "La contraseña no puede estar vacía"
                                esValido = false
                            } else if (password.length < 6) {
                                passwordError = "Mínimo 6 caracteres"
                                esValido = false
                            }

                            if (esValido) {
                                onIngresarAlDashboard()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onNavegarAlRegistro) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = Color(0xFF4B6CB7),
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(onClick = onVolver) {
                Text("← Volver al inicio", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
