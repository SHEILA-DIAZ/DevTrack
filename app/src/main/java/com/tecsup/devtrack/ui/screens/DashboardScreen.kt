package com.tecsup.devtrack.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import com.tecsup.devtrack.navigation.Routes
import kotlinx.coroutines.launch

/**
 * Dashboard principal de DevTrack - Versión Panel de Control SaaS.
 * COMENTARIO PARA SUSTENTACIÓN: Implementa una jerarquía visual avanzada que transforma los datos de Room
 * en información estratégica (KPIs) mediante gráficos personalizados en Compose.
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

    // Cálculo dinámico de métricas (Persistencia reactiva)
    val totalProyectos = proyectos.size
    val enDesarrollo = proyectos.count { it.estado == "En desarrollo" }
    val finalizados = proyectos.count { it.estado == "Finalizado" }
    val planificados = proyectos.count { it.estado == "Planificado" }

    val totalTareas = tareas.size
    
    val porcentajeAvance = if (totalProyectos > 0) finalizados.toFloat() / totalProyectos else 0f

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                MenuHeader()
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(16.dp))
                
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
                    title = { Text("DevTrack Central", fontWeight = FontWeight.Bold) },
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
                // Decoración de fondo premium
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 100.dp, y = (-80).dp)
                        .background(Color(0xFF4B6CB7).copy(alpha = 0.05f), CircleShape)
                        .blur(50.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // COMENTARIO PARA SUSTENTACIÓN: Encabezado personalizado de bienvenida.
                    DashboardHeader()

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tarjeta Principal: Productividad
                    ProductivityCard(porcentajeAvance)

                    Spacer(modifier = Modifier.height(24.dp))

                    if (totalProyectos == 0) {
                        EmptyDashboardPlaceholder()
                    } else {
                        // Sección de métricas rápidas (Grid)
                        Text("Estadísticas rápidas", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                MiniStatCard("Proyectos", totalProyectos.toString(), Icons.Default.Info, Color(0xFF4B6CB7))
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                MiniStatCard("Tareas", totalTareas.toString(), Icons.AutoMirrored.Filled.List, Color(0xFF2D3E9F))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                MiniStatCard("En desarrollo", enDesarrollo.toString(), Icons.Default.Build, Color(0xFFF9A825))
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                MiniStatCard("Finalizados", finalizados.toString(), Icons.Default.CheckCircle, Color(0xFF2E7D32))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Gráfico de Barras Comparativo
                        Text("Distribución de proyectos", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(12.dp))
                        BarChartCard(planificados, enDesarrollo, finalizados)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Acción rápida
                    Button(
                        onClick = { onNavegar(Routes.LISTA_PROYECTOS) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Gestionar Proyectos", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun DashboardHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Hola 👋", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Text(
                "Usuario DevTrack",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1A1C1E)
            )
            Text("Bienvenido nuevamente", style = MaterialTheme.typography.bodySmall, color = Color(0xFF4B6CB7))
        }
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = Color(0xFF4B6CB7).copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF4B6CB7))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("DT", color = Color(0xFF4B6CB7), fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun ProductivityCard(avance: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("🎯 Productividad", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Tu avance general", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { avance },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape),
                    color = Color(0xFF4B6CB7),
                    trackColor = Color(0xFFF1F3F4)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${(avance * 100).toInt()}% completado",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B6CB7)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            CircularProgressIndicator(
                progress = { avance },
                modifier = Modifier.size(60.dp),
                strokeWidth = 6.dp,
                color = Color(0xFF4B6CB7)
            )
        }
    }
}

@Composable
fun MiniStatCard(titulo: String, valor: String, icono: ImageVector, color: Color) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = valor, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text(text = titulo, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
fun BarChartCard(plan: Int, dev: Int, fin: Int) {
    val max = maxOf(plan, dev, fin, 1).toFloat()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Bar(plan.toFloat() / max, "Plan.", Color(0xFF4B6CB7))
            Bar(dev.toFloat() / max, "Desar.", Color(0xFFF9A825))
            Bar(fin.toFloat() / max, "Fin.", Color(0xFF2E7D32))
        }
    }
}

@Composable
fun Bar(progreso: Float, etiqueta: String, color: Color) {
    val animHeight by animateFloatAsState(targetValue = progreso, animationSpec = tween(1000))
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(30.dp)
                .fillMaxHeight(animHeight.coerceAtLeast(0.1f))
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(etiqueta, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun EmptyDashboardPlaceholder() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(32.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Comienza creando tu primer proyecto", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Tus estadísticas aparecerán aquí automáticamente.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun MenuHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Color(0xFF2D3E9F), Color(0xFF4B6CB7))))
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.2f)) {
                Box(contentAlignment = Alignment.Center) {
                    Text("DT", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("DevTrack", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Text("Gestión de proyectos", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun DrawerOption(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null, tint = color) },
        label = { Text(label, fontWeight = FontWeight.Medium) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
    )
}
