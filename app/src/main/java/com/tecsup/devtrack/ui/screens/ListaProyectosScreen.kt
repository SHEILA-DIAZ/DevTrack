package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProyectosScreen(
    viewModel: ProyectoViewModel,
    tareas: List<Tarea>,
    onVolver: () -> Unit,
    onVerTareas: (Int) -> Unit,
    onEditarProyecto: () -> Unit,
    onVerDetalle: (Proyecto) -> Unit,
    onAgregarProyecto: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }
    var proyectoParaEliminar by remember { mutableStateOf<Proyecto?>(null) }

    val filters = listOf("Todos", "Pendiente", "En Progreso", "Completado")

    // Lógica de filtrado segura
    val proyectosFiltrados = try {
        uiState.proyectos.filter { proyecto ->
            val matchesSearch = proyecto.nombre.contains(searchText, ignoreCase = true) || 
                              proyecto.descripcion.contains(searchText, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                "Pendiente" -> proyecto.estado == "Planificado" || proyecto.estado == "Pendiente"
                "En Progreso" -> proyecto.estado == "En desarrollo" || proyecto.estado == "En proceso"
                "Completado" -> proyecto.estado == "Finalizado" || proyecto.estado == "Completado"
                else -> true
            }
            matchesSearch && matchesFilter
        }
    } catch (e: Exception) {
        android.util.Log.e("DEVTRACK_ERROR", "Error al filtrar proyectos", e)
        emptyList()
    }

    val devTrackBlue = Color(0xFF4B6CB7)
    val devTrackDark = Color(0xFF0D1B3E)
    val lightGray = Color(0xFFF1F4F9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Proyectos", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = devTrackBlue)
                    }
                },
                actions = {
                    IconButton(
                        onClick = onAgregarProyecto,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .background(devTrackBlue, CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo Proyecto", tint = Color.White)
                    }
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
        ) {
            // Ilustración Superior y Título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mis\nProyectos",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = devTrackDark,
                            lineHeight = 36.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.width(40.dp).height(4.dp).background(devTrackBlue))
                    }
                    AsyncImage(
                        model = "https://img.freepik.com/free-vector/project-management-concept-illustration_114360-1437.jpg",
                        contentDescription = null,
                        modifier = Modifier.size(140.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Barra de Búsqueda
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                placeholder = { Text("Buscar proyectos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = devTrackBlue
                ),
                singleLine = true
            )

            // Filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = devTrackBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Lista de Proyectos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (proyectosFiltrados.isEmpty()) {
                    item {
                        EmptyProjectsState()
                    }
                } else {
                    items(proyectosFiltrados) { proyecto ->
                        ProyectoCard(
                            proyecto = proyecto,
                            tareasRelacionadas = try { tareas.filter { it.proyectoId == proyecto.id } } catch (e: Exception) { emptyList() },
                            onVerDetalle = { onVerDetalle(proyecto) },
                            onVerTareas = { onVerTareas(proyecto.id) },
                            onEditar = { 
                                viewModel.seleccionarProyecto(proyecto)
                                onEditarProyecto()
                            },
                            onEliminar = { proyectoParaEliminar = proyecto }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }

        // Diálogo de Confirmación de Eliminación
        if (proyectoParaEliminar != null) {
            AlertDialog(
                onDismissRequest = { proyectoParaEliminar = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar el proyecto \"${proyectoParaEliminar?.nombre}\"? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            proyectoParaEliminar?.let { viewModel.eliminarProyecto(it) }
                            proyectoParaEliminar = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { proyectoParaEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun EmptyProjectsState() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(32.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("No se encontraron proyectos", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Intenta con otra búsqueda o agrega un nuevo proyecto.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ProyectoCard(
    proyecto: Proyecto,
    tareasRelacionadas: List<Tarea>,
    onVerDetalle: () -> Unit,
    onVerTareas: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val totalTareas = tareasRelacionadas.size
    val tareasCompletadas = tareasRelacionadas.count { it.estado == "Completada" || it.estado == "Finalizada" }
    
    // Cálculo de progreso seguro (nunca división entre cero)
    val progreso = if (totalTareas > 0) tareasCompletadas.toFloat() / totalTareas else 0f
    
    val devTrackBlue = Color(0xFF4B6CB7)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icono del proyecto
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = devTrackBlue.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Build, contentDescription = null, tint = devTrackBlue)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(proyecto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(proyecto.descripcion.ifBlank { "Sin descripción" }, fontSize = 12.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                // Badge de Estado
                val statusColor = when (proyecto.estado) {
                    "Finalizado", "Completado" -> Color(0xFF2E7D32)
                    "En desarrollo", "En proceso" -> Color(0xFF1976D2)
                    else -> Color(0xFFF9A825)
                }
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(statusColor, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(proyecto.estado.ifBlank { "Sin estado" }, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tecnologías seguras
            val techs = proyecto.tecnologias.split(",").filter { it.isNotBlank() }
            if (techs.isEmpty()) {
                Text("Sin tecnologías", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
            } else {
                SimpleFlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalGap = 6.dp,
                    verticalGap = 6.dp
                ) {
                    techs.take(3).forEach { tech ->
                        Surface(
                            color = Color(0xFFF1F3F4),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                tech.trim(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    if (techs.size > 3) {
                        Text("+${techs.size - 3}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp), color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fechas seguras
            val fechaIni = proyecto.fechaInicio.ifBlank { "--/--/----" }
            val fechaLim = proyecto.fechaLimite.ifBlank { "--/--/----" }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$fechaIni → $fechaLim", fontSize = 11.sp, color = Color.DarkGray)
                }
                Text("$tareasCompletadas/$totalTareas tareas - ${(progreso * 100).toInt()}%", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de Progreso segura
            LinearProgressIndicator(
                progress = { progreso.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = devTrackBlue,
                trackColor = Color(0xFFF1F3F4)
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F3F4))
            Spacer(modifier = Modifier.height(12.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(Icons.Default.Info, "Ver detalle", Color(0xFFE3F2FD), devTrackBlue, Modifier.weight(1f), onVerDetalle)
                ActionButton(Icons.AutoMirrored.Filled.List, "Tareas", Color(0xFFE8EAF6), Color(0xFF3F51B5), Modifier.weight(1f), onVerTareas)
                ActionButton(Icons.Default.Edit, "Editar", Color(0xFFE8F5E9), Color(0xFF2E7D32), Modifier.weight(1f), onEditar)
                ActionButton(Icons.Default.Delete, "Eliminar", Color(0xFFFFEBEE), Color(0xFFD32F2F), Modifier.weight(1f), onEliminar)
            }
        }
    }
}

@Composable
fun ActionButton(icon: ImageVector, label: String, bgColor: Color, iconColor: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = iconColor)
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, fontSize = 10.sp, color = iconColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    horizontalGap: Dp = 8.dp,
    verticalGap: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }
        var currentX = 0
        var currentY = 0
        var maxRowHeight = 0
        val layoutWidth = constraints.maxWidth
        
        val positions = mutableListOf<Pair<Int, Int>>()
        
        placeables.forEach { placeable ->
            if (currentX + placeable.width > layoutWidth && currentX > 0) {
                currentX = 0
                currentY += maxRowHeight + verticalGap.roundToPx()
                maxRowHeight = 0
            }
            positions.add(currentX to currentY)
            currentX += placeable.width + horizontalGap.roundToPx()
            maxRowHeight = maxOf(maxRowHeight, placeable.height)
        }
        
        layout(layoutWidth, if (placeables.isEmpty()) 0 else currentY + maxRowHeight) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(positions[index].first, positions[index].second)
            }
        }
    }
}
