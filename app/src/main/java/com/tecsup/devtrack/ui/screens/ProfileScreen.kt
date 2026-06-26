package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    proyectos: List<Proyecto>,
    tareas: List<Tarea>,
    onVolver: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val devTrackBlue = Color(0xFF4B6CB7)
    val devTrackDark = Color(0xFF0D1B3E)
    val lightGray = Color(0xFFF1F4F9)

    val displayName = uiState.userName ?: "Usuario DevTrack"
    val initials = if(displayName.isNotBlank()) {
        displayName.split(" ").filter { it.isNotBlank() }.map { it.first().uppercase() }.take(2).joinToString("")
    } else "DT"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = devTrackBlue)
                    }
                },
                actions = {
                    // Eliminado el icono de editar según requerimiento
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightGray)
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta Azul Principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = devTrackDark)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Decoración de fondo
                    Surface(
                        modifier = Modifier.size(150.dp).offset(x = 180.dp, y = (-20).dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.05f)
                    ) {}

                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                Surface(
                                    modifier = Modifier.size(80.dp),
                                    shape = CircleShape,
                                    color = Color.White.copy(alpha = 0.1f),
                                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White.copy(alpha = 0.3f))
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(initials, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Surface(
                                    modifier = Modifier.size(22.dp).offset(x = (-2).dp, y = (-2).dp),
                                    shape = CircleShape,
                                    color = Color(0xFF2E7D32),
                                    border = androidx.compose.foundation.BorderStroke(2.dp, devTrackDark)
                                ) {}
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Column {
                                Text(displayName, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                Text(uiState.userEmail ?: "", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            ProfileStatItem(proyectos.size.toString(), "Proyectos", Modifier.weight(1f))
                            ProfileStatItem(tareas.size.toString(), "Tareas", Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Actividad reciente", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = devTrackDark)
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Actividad con datos reales
            if (proyectos.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Aún no hay actividad reciente.", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("Crea tu primer proyecto para comenzar a registrar avances.", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    proyectos.take(3).forEach { proyecto ->
                        ActivityItem(
                            icon = Icons.Default.Add, 
                            action = "Creaste", 
                            target = proyecto.nombre, 
                            time = "Estado: ${proyecto.estado}", 
                            color = devTrackBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Cerrar Sesión
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { 
                        viewModel.logout()
                        onLogout()
                    },
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFEBEE)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFD32F2F))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Cerrar sesión", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileStatItem(value: String, label: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.08f)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                if(label == "Proyectos") Icons.Default.Info else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if(label == "Proyectos") Color(0xFF4B6CB7) else Color(0xFF3F51B5),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }
    }
}

@Composable
fun ActivityItem(icon: ImageVector, action: String, target: String, time: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(action, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(" - $target", fontSize = 13.sp, color = Color.Gray)
                }
                Text(time, fontSize = 12.sp, color = Color.LightGray)
            }
        }
    }
}
