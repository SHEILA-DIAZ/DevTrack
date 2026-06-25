package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/**
 * Pantalla de bienvenida (Splash) de DevTrack - Iteración Premium Onboarding.
 * COMENTARIO PARA SUSTENTACIÓN: Diseño inspirado en herramientas de productividad líderes (Linear/Notion).
 * Eleva la percepción de calidad del producto mediante jerarquía visual y elementos decorativos.
 */
@Composable
fun SplashScreen(
    onNavegarAlLogin: () -> Unit,
    onNavegarAlRegistro: () -> Unit,
    isAuthenticated: Boolean = false,
    onNavigateToDashboard: () -> Unit = {}
) {
    // Redirección automática si ya hay sesión iniciada
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            onNavigateToDashboard()
        }
    }

    // COMENTARIO PARA SUSTENTACIÓN: Mantenemos el degradado identitario de DevTrack.
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2D3E9F), // Azul profundo
            Color(0xFF4B6CB7), // Azul intermedio
            Color(0xFF6DD5FA), // Sky Blue
            Color(0xFFF8F9FA)  // Fondo claro inferior
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Elementos decorativos abstractos de fondo
        BackgroundDecorations()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Cabecera con elementos flotantes
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                // Iconos decorativos flotantes (Usando iconos garantizados en el set default)
                FloatingIcon(Icons.Default.Build, Modifier.align(Alignment.TopStart).offset(x = 10.dp, y = 10.dp))
                FloatingIcon(Icons.Default.Info, Modifier.align(Alignment.TopEnd).offset(x = (-10).dp, y = 30.dp))
                FloatingIcon(Icons.Default.Edit, Modifier.align(Alignment.BottomStart).offset(x = 0.dp, y = (-10).dp))
                FloatingIcon(Icons.Default.Star, Modifier.align(Alignment.BottomEnd).offset(x = 0.dp, y = (-20).dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Mini avatar DT con estilo moderno
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("DT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Bienvenido a",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 2.sp
                    )
                    
                    Text(
                        text = "DevTrack",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 48.sp,
                            color = Color.White,
                            letterSpacing = (-1.5).sp
                        )
                    )
                    
                    // Línea decorativa minimalista
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(4.dp)
                            .background(Color.White, RoundedCornerShape(2.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Organiza, gestiona y da seguimiento a tus proyectos tecnológicos desde un solo lugar.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(horizontal = 20.dp),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Tarjeta de imagen premium con borde refinado y sombra suave.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)), RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            ) {
                AsyncImage(
                    model = "https://i.pinimg.com/736x/9f/06/8b/9f068b54092b1089f8d016f02a585e70.jpg",
                    contentDescription = "Preview de la plataforma",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(40.dp))

            // COMENTARIO PARA SUSTENTACIÓN: Botones con estilo moderno y jerarquía clara.
            Button(
                onClick = onNavegarAlLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D3E9F)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Iniciar sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavegarAlRegistro,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.5.dp, Color(0xFF2D3E9F)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2D3E9F),
                    containerColor = Color.White
                )
            ) {
                Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FloatingIcon(icon: ImageVector, modifier: Modifier) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = Color.White.copy(alpha = 0.3f),
        modifier = modifier.size(32.dp)
    )
}

@Composable
fun BackgroundDecorations() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
                .blur(40.dp)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 80.dp, y = 100.dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
                .blur(30.dp)
        )
        // Patrón de puntos decorativos sutiles
        Column(
            modifier = Modifier.padding(40.dp).alpha(0.1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(3) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(3) {
                        Box(modifier = Modifier.size(4.dp).background(Color.White, CircleShape))
                    }
                }
            }
        }
    }
}
