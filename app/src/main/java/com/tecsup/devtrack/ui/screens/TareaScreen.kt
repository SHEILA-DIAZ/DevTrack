package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.viewmodel.TareaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaScreen(
    viewModel: TareaViewModel,
    proyectos: List<Proyecto>,
    proyectoId: Int,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf("Todas") }
    var menuExpandidoPrioridad by remember { mutableStateOf(false) }
    var menuExpandidoProyectos by remember { mutableStateOf(false) }
    var tareaParaEliminar by remember { mutableStateOf<Tarea?>(null) }

    val filters = listOf("Todas", "Planificadas", "En Desarrollo", "Finalizadas")
    val prioridades = listOf("Baja", "Media", "Alta")

    LaunchedEffect(proyectoId) {
        if (proyectoId > 0) {
            viewModel.cargarTareasPorProyecto(proyectoId)
        } else {
            viewModel.cargarTodasLasTareas()
        }
    }

    val planificadas = uiState.tareas.count { it.estado == "Pendiente" || it.estado == "Planificada" }
    val enDesarrollo = uiState.tareas.count { it.estado == "En proceso" || it.estado == "En desarrollo" }
    val finalizadas = uiState.tareas.count { it.estado == "Completada" || it.estado == "Finalizada" }

    val tareasFiltradas = uiState.tareas.filter { tarea ->
        when (selectedFilter) {
            "Planificadas" -> tarea.estado == "Pendiente" || tarea.estado == "Planificada"
            "En Desarrollo" -> tarea.estado == "En proceso" || tarea.estado == "En desarrollo"
            "Finalizadas" -> tarea.estado == "Completada" || tarea.estado == "Finalizada"
            else -> true
        }
    }

    val devTrackBlue = Color(0xFF4B6CB7)
    val lightGray = Color(0xFFF1F4F9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Mis Tareas", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = devTrackBlue)
                    }
                },
                actions = {
                    TextButton(onClick = onVolver) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp), tint = devTrackBlue)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Volver", color = devTrackBlue, fontWeight = FontWeight.Bold)
                        }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResumenCard("Planificadas", planificadas.toString(), Color(0xFFFFF9C4), Color(0xFFF9A825), Modifier.weight(1f))
                ResumenCard("En Desarrollo", enDesarrollo.toString(), Color(0xFFE3F2FD), Color(0xFF1976D2), Modifier.weight(1f))
                ResumenCard("Finalizadas", finalizadas.toString(), Color(0xFFE8F5E9), Color(0xFF2E7D32), Modifier.weight(1f))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Formulario
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = devTrackBlue)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (uiState.isEditing) "Editar Tarea" else "Registrar Nueva Tarea", 
                                        fontWeight = FontWeight.Bold, 
                                        color = devTrackBlue
                                    )
                                }
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (proyectos.isEmpty()) {
                                Text(
                                    "Primero debes crear un proyecto para registrar tareas.",
                                    color = Color.Red,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                OutlinedTextField(
                                    value = uiState.nombre,
                                    onValueChange = { viewModel.actualizarNombre(it) },
                                    placeholder = { Text("Nombre de la tarea") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    enabled = proyectos.isNotEmpty()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = uiState.descripcion,
                                    onValueChange = { viewModel.actualizarDescripcion(it) },
                                    placeholder = { Text("Descripción (opcional)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    enabled = proyectos.isNotEmpty()
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Selector de Proyecto (solo si no viene de uno específico)
                                if (proyectoId == 0) {
                                    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                                        val proyectoSeleccionado = proyectos.find { it.id == uiState.proyectoId }
                                        OutlinedCard(
                                            onClick = { menuExpandidoProyectos = true },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFF8F9FA))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(proyectoSeleccionado?.nombre ?: "Seleccionar Proyecto", fontSize = 14.sp)
                                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                            }
                                        }
                                        DropdownMenu(
                                            expanded = menuExpandidoProyectos,
                                            onDismissRequest = { menuExpandidoProyectos = false }
                                        ) {
                                            proyectos.forEach { p ->
                                                DropdownMenuItem(
                                                    text = { Text(p.nombre) },
                                                    onClick = { 
                                                        viewModel.cargarTareasPorProyecto(p.id)
                                                        menuExpandidoProyectos = false 
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Selector Prioridad
                                    Box(modifier = Modifier.weight(1f)) {
                                        OutlinedCard(
                                            onClick = { menuExpandidoPrioridad = true },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFF8F9FA))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Prioridad ${uiState.prioridad}", fontSize = 14.sp)
                                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                            }
                                        }
                                        DropdownMenu(
                                            expanded = menuExpandidoPrioridad,
                                            onDismissRequest = { menuExpandidoPrioridad = false }
                                        ) {
                                            prioridades.forEach { p ->
                                                DropdownMenuItem(
                                                    text = { Text(p) },
                                                    onClick = { viewModel.actualizarPrioridad(p); menuExpandidoPrioridad = false }
                                                )
                                            }
                                        }
                                    }
                                    
                                    Button(
                                        onClick = { viewModel.guardarTarea() },
                                        modifier = Modifier.weight(0.8f).height(48.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = devTrackBlue),
                                        enabled = proyectos.isNotEmpty()
                                    ) {
                                        Text(if (uiState.isEditing) "Guardar" else "Agregar", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                }

                items(tareasFiltradas) { tarea ->
                    TareaItem(
                        tarea = tarea,
                        onEliminar = { tareaParaEliminar = tarea },
                        onEditar = { viewModel.seleccionarTarea(tarea) },
                        onCompletar = { 
                            viewModel.seleccionarTarea(tarea)
                            viewModel.actualizarEstado("Completada")
                            viewModel.guardarTarea()
                        }
                    )
                }
                
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }

        if (tareaParaEliminar != null) {
            AlertDialog(
                onDismissRequest = { tareaParaEliminar = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Deseas eliminar esta tarea?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            tareaParaEliminar?.let { viewModel.eliminarTarea(it) }
                            tareaParaEliminar = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { tareaParaEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun ResumenCard(title: String, count: String, bgColor: Color, textColor: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor)
            Text(text = "$count tareas", fontSize = 10.sp, color = textColor.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun TareaItem(tarea: Tarea, onEliminar: () -> Unit, onEditar: () -> Unit, onCompletar: () -> Unit) {
    val isFinalizada = tarea.estado == "Completada" || tarea.estado == "Finalizada"
    val statusColor = when (tarea.estado) {
        "Completada", "Finalizada" -> Color(0xFF2E7D32)
        "En proceso", "En desarrollo" -> Color(0xFF1976D2)
        else -> Color(0xFFF9A825)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(statusColor.copy(alpha = 0.1f))
                    .clickable { if (!isFinalizada) onCompletar() },
                contentAlignment = Alignment.Center
            ) {
                if (isFinalizada) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = statusColor, modifier = Modifier.size(18.dp))
                } else {
                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(statusColor))
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textDecoration = if (isFinalizada) TextDecoration.LineThrough else null,
                    color = if (isFinalizada) Color.Gray else Color.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Proyecto #${tarea.proyectoId}", fontSize = 11.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val priorColor = when(tarea.prioridad) {
                        "Alta" -> Color(0xFFD32F2F)
                        "Media" -> Color(0xFFF9A825)
                        else -> Color(0xFF2E7D32)
                    }
                    Surface(color = priorColor.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                        Text(tarea.prioridad, color = priorColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                    Surface(color = statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                        Text(tarea.estado, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
            
            Row {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF1976D2))
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F))
                }
            }
        }
    }
}
