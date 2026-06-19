package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.navigation.Routes
import kotlinx.coroutines.launch

/**
 * Dashboard principal con estadísticas dinámicas y menú lateral (Drawer).
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

    // Cálculo de estadísticas de proyectos
    // COMENTARIO PARA SUSTENTACIÓN: Se obtienen las métricas filtrando la lista de proyectos por su estado.
    val totalProyectos = proyectos.size
    val enDesarrollo = proyectos.count { it.estado == "En desarrollo" }
    val finalizados = proyectos.count { it.estado == "Finalizado" }
    val planificados = proyectos.count { it.estado == "Planificado" }

    // Cálculo de estadísticas de tareas
    val totalTareas = tareas.size
    val completadas = tareas.count { it.estado == "Completada" }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "DevTrack Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                // Navegación desde el menú lateral
                // COMENTARIO PARA SUSTENTACIÓN: El menú lateral centraliza el acceso a todas las secciones.
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Agregar proyecto") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        onNavegar(Routes.PROYECTOS) 
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("Lista de proyectos") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        onNavegar(Routes.PROYECTOS) 
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    label = { Text("Detalle del proyecto") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        onNavegar(Routes.DETALLE_PROYECTO) 
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Share, contentDescription = null) },
                    label = { Text("Recursos Tecnológicos") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        onNavegar(Routes.RECURSOS) 
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    label = { Text("Mi Perfil") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        onNavegar(Routes.PERFIL) 
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Resumen general de tus proyectos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Gráfico de avance (Placeholder con lógica real)
                // COMENTARIO PARA SUSTENTACIÓN: Se muestra el progreso basado en proyectos finalizados vs total.
                Card(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val progreso = if(totalProyectos > 0) finalizados.toFloat() / totalProyectos else 0f
                            CircularProgressIndicator(
                                progress = { progreso },
                                modifier = Modifier.size(80.dp),
                                strokeWidth = 8.dp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Gráfico de avance de proyectos",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${(progreso * 100).toInt()}% completado",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (totalProyectos == 0) {
                    Text(
                        text = "Aún no tienes proyectos registrados.",
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = "Estadísticas de Proyectos",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    EstadisticaCard("Total de proyectos", totalProyectos.toString(), Icons.Default.Info)
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            EstadisticaCard("Planificados", planificados.toString(), Icons.Default.DateRange)
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            EstadisticaCard("En desarrollo", enDesarrollo.toString(), Icons.Default.Build)
                        }
                    }
                    
                    EstadisticaCard("Finalizados", finalizados.toString(), Icons.Default.Check)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Resumen de Tareas",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            EstadisticaCard("Total Tareas", totalTareas.toString(), Icons.AutoMirrored.Filled.List)
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            EstadisticaCard("Completadas", completadas.toString(), Icons.Default.ThumbUp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { onNavegar(Routes.PROYECTOS) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Gestionar Proyectos")
                }
            }
        }
    }
}

@Composable
fun EstadisticaCard(
    titulo: String,
    valor: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icono, 
                contentDescription = null, 
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = titulo, style = MaterialTheme.typography.bodySmall)
                Text(text = valor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }
    }
}
