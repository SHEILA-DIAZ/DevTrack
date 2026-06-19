package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.navigation.Routes
import kotlinx.coroutines.launch

/**
 * Dashboard principal con estadísticas dinámicas y menú lateral Premium (Drawer).
 * COMENTARIO PARA SUSTENTACIÓN: Implementa ModalNavigationDrawer para una navegación SaaS eficiente,
 * centralizando el acceso a las métricas y herramientas de gestión.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    proyectos: List<Proyecto>,
    tareas: List<Tarea>,
    onNavegar: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val totalProyectos = proyectos.size
    val enDesarrollo = proyectos.count { it.estado == "En desarrollo" }
    val finalizados = proyectos.count { it.estado == "Finalizado" }
    val planificados = proyectos.count { it.estado == "Planificado" }

    val totalTareas = tareas.size
    val completadas = tareas.count { it.estado == "Completada" }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                // Encabezado del Menú Lateral
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F9FA))
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = Color(0xFF4B6CB7),
                            shadowElevation = 4.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("DT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("DevTrack", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1C1E))
                            Text("Gestión de proyectos", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }
                }
                
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                
                Spacer(modifier = Modifier.height(16.dp))

                // Opciones del Menú Premium
                DrawerOption(Icons.Default.AddCircle, "Agregar proyecto", Color(0xFF4B6CB7)) {
                    scope.launch { drawerState.close() }
                    onNavegar(Routes.PROYECTOS)
                }
                DrawerOption(Icons.AutoMirrored.Filled.List, "Lista de proyectos", Color(0xFF4B6CB7)) {
                    scope.launch { drawerState.close() }
                    onNavegar(Routes.LISTA_PROYECTOS)
                }
                DrawerOption(Icons.Default.CheckCircle, "Lista de tareas", Color(0xFF4B6CB7)) {
                    scope.launch { drawerState.close() }
                    onNavegar(Routes.tareas(0))
                }
                DrawerOption(Icons.Default.Share, "Recursos Tecnológicos", Color(0xFF4B6CB7)) {
                    scope.launch { drawerState.close() }
                    onNavegar(Routes.RECURSOS)
                }
                DrawerOption(Icons.Default.AccountCircle, "Mi Perfil", Color(0xFF4B6CB7)) {
                    scope.launch { drawerState.close() }
                    onNavegar(Routes.PROFILE)
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFF4B6CB7))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(padding)
            ) {
                // Fondo decorativo sutil
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 100.dp, y = (-50).dp)
                        .background(Color(0xFF4B6CB7).copy(alpha = 0.05f), CircleShape)
                        .blur(40.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Resumen general de tus proyectos",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Gráfico de progreso dinámico
                    Card(
                        modifier = Modifier.fillMaxWidth().height(180.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val progreso = if(totalProyectos > 0) finalizados.toFloat() / totalProyectos else 0f
                                CircularProgressIndicator(
                                    progress = { progreso },
                                    modifier = Modifier.size(70.dp),
                                    strokeWidth = 8.dp,
                                    color = Color(0xFF4B6CB7)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Progreso General", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                Text("${(progreso * 100).toInt()}% completado", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (totalProyectos == 0) {
                        Text(
                            text = "Aún no tienes proyectos registrados.",
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.LightGray
                        )
                    } else {
                        SectionHeader("Estadísticas de Proyectos")
                        EstadisticaCard("Total de proyectos", totalProyectos.toString(), Icons.Default.Info)
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                EstadisticaCard("Planificados", planificados.toString(), Icons.Default.DateRange)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                EstadisticaCard("En desarrollo", enDesarrollo.toString(), Icons.Default.Build)
                            }
                        }
                        
                        EstadisticaCard("Finalizados", finalizados.toString(), Icons.Default.CheckCircle)

                        Spacer(modifier = Modifier.height(20.dp))

                        SectionHeader("Resumen de Tareas")
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                EstadisticaCard("Total Tareas", totalTareas.toString(), Icons.AutoMirrored.Filled.List)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                EstadisticaCard("Completadas", completadas.toString(), Icons.Default.ThumbUp)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = { onNavegar(Routes.LISTA_PROYECTOS) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("Gestionar Proyectos", fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun DrawerOption(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp)) },
        label = { Text(label, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
    )
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF4B6CB7),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun EstadisticaCard(
    titulo: String,
    valor: String,
    icono: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icono, 
                contentDescription = null, 
                tint = Color(0xFF4B6CB7),
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = titulo, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(text = valor, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
            }
        }
    }
}
