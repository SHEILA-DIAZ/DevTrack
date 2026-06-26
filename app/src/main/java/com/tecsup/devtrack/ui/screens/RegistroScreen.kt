package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.devtrack.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: AuthViewModel,
    onVolver: () -> Unit,
    onNavegarAlLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onNavigateToDashboard()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            // Espera breve para que el usuario lea el mensaje de éxito antes de redirigir
            delay(2000)
            onNavegarAlLogin()
        }
    }

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val primaryGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF3F51B5), Color(0xFF2D3E9F))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header Azul Oscuro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF0D1B3E))
                .statusBarsPadding()
        ) {
            IconButton(
                onClick = onVolver,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            
            // Decoraciones de fondo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Crea tu cuenta 🚀",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Empieza a gestionar tus proyectos tecnológicos hoy",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.7f)
                    )
                )
            }
        }

        // Formulario
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Nombre Completo
            LabelText("NOMBRE COMPLETO")
            RegisterField(
                value = nombre,
                onValueChange = { nombre = it; nombreError = null },
                placeholder = "Tu nombre completo",
                icon = Icons.Default.Person,
                error = nombreError,
                isError = nombreError != null
            )

            // Campo: Correo Electrónico
            LabelText("CORREO ELECTRÓNICO")
            RegisterField(
                value = correo,
                onValueChange = { correo = it; correoError = null },
                placeholder = "tu@correo.com",
                icon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                error = correoError,
                isError = correoError != null
            )

            // Campo: Contraseña
            LabelText("CONTRASEÑA")
            RegisterField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                placeholder = "Mínimo 8 caracteres",
                icon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Info else Icons.Default.Info,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                error = passwordError,
                isError = passwordError != null
            )

            // Campo: Confirmar Contraseña
            LabelText("CONFIRMAR CONTRASEÑA")
            RegisterField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmPasswordError = null },
                placeholder = "Repite tu contraseña",
                icon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Info else Icons.Default.Info,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                error = confirmPasswordError,
                isError = confirmPasswordError != null
            )

            Spacer(modifier = Modifier.height(8.dp))

                    // Botón Registrarse
                    Button(
                        onClick = {
                            var esValido = true
                            
                            // Validación de Nombre
                            if (nombre.isBlank()) {
                                nombreError = "Ingresa tu nombre completo."
                                esValido = false
                            } else if (nombre.length < 3) {
                                nombreError = "El nombre debe tener al menos 3 caracteres."
                                esValido = false
                            }

                            // Validación de Correo con Regex Flexible
                            val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
                            if (correo.isBlank()) {
                                correoError = "Ingresa tu correo electrónico."
                                esValido = false
                            } else if (!correo.matches(emailPattern.toRegex())) {
                                correoError = "Ingresa un correo electrónico válido."
                                esValido = false
                            }

                            // Validación de Contraseña
                            if (password.isBlank()) {
                                passwordError = "Ingresa una contraseña."
                                esValido = false
                            } else if (password.length < 8) {
                                passwordError = "La contraseña debe tener al menos 8 caracteres."
                                esValido = false
                            }

                            // Validación de Confirmación
                            if (confirmPassword.isBlank()) {
                                confirmPasswordError = "Confirma tu contraseña."
                                esValido = false
                            } else if (password != confirmPassword) {
                                confirmPasswordError = "Las contraseñas no coinciden."
                                esValido = false
                            }

                            if (esValido) { 
                                viewModel.register(nombre, correo, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(primaryGradient),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        enabled = !uiState.isLoading
                    ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Crear mi cuenta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }

            // Mensajes de Firebase
            uiState.errorMessage?.let {
                Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            uiState.successMessage?.let {
                Text(text = it, color = Color(0xFF2E7D32), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }

            Text(
                text = "Al registrarte, aceptas los Términos de uso y la Política de privacidad",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(
                onClick = onNavegarAlLogin,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "¿Ya tienes cuenta? Iniciar sesión",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D1B3E)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LabelText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D1B3E),
            letterSpacing = 0.5.sp
        ),
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun RegisterField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    error: String? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = Color.LightGray) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFF3F51B5)) },
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3F51B5),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            errorBorderColor = Color.Red
        ),
        isError = isError,
        supportingText = { error?.let { Text(it, color = Color.Red) } },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        singleLine = true
    )
}
