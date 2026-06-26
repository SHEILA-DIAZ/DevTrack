package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.viewmodel.ProyectoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProyectoScreen(
    viewModel: ProyectoViewModel,
    tareas: List<Tarea>,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val tareasDelProyecto = tareas.filter { it.proyectoId != 0 } // Simulado ya que no tenemos el ID seleccionado explícito en el UIState actual, pero el ViewModel carga los campos.

    val completadas = tareasDelProyecto.count { it.estado == "Completada" || it.estado == "Finalizada" }
    val porcentaje = if (tareasDelProyecto.isNotEmpty()) completadas.toFloat() / tareasDelProyecto.size else 0f

    val devTrackBlue = Color(0xFF4B6CB7)
    val backgroundBlue = Color(0xFF1E2632)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Proyecto", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = backgroundBlue
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = "https://img.freepik.com/free-vector/data-extraction-concept-illustration_114360-4876.jpg",
                        contentDescription = null,
                        modifier = Modifier.size(150.dp).align(Alignment.CenterEnd).padding(end = 8.dp),
                        alpha = 0.5f
                    )
                    
                    Column(modifier = Modifier.padding(24.dp)) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = devTrackBlue
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.nombre,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFF1976D2).copy(alpha = 0.2f),
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(8.dp).background(Color(0xFF1976D2), CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(uiState.estado, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Información del proyecto", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            InfoItem(Icons.Default.Info, "Nombre", uiState.nombre)
            InfoItem(Icons.Default.Info, "Descripción", uiState.descripcion)
            
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Build, contentDescription = null, tint = devTrackBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Tecnologías", color = Color.Gray, fontSize = 12.sp)
                    SimpleFlowRow(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalGap = 8.dp,
                        verticalGap = 8.dp
                    ) {
                        uiState.tecnologias.split(",").filter { it.isNotBlank() }.forEach { tech ->
                            Surface(
                                color = devTrackBlue.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    tech.trim(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            InfoItem(Icons.Default.DateRange, "Fecha de inicio", uiState.fechaInicio)
            InfoItem(Icons.Default.DateRange, "Fecha límite", uiState.fechaLimite)
            InfoItem(Icons.Default.Info, "Observaciones", "Proyecto en curso gestionado por DevTrack.")

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Progreso del proyecto", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("$completadas/${tareasDelProyecto.size} tareas completadas", color = Color.Gray, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = { porcentaje },
                                modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                                color = devTrackBlue,
                                trackColor = Color.White.copy(alpha = 0.1f)
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Box(contentAlignment = Alignment.Center) {
                            Canvas(modifier = Modifier.size(80.dp)) {
                                drawArc(
                                    color = Color.White.copy(alpha = 0.1f),
                                    startAngle = 0f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    style = Stroke(width = 8.dp.toPx())
                                )
                                drawArc(
                                    color = devTrackBlue,
                                    startAngle = -90f,
                                    sweepAngle = 360f * porcentaje,
                                    useCenter = false,
                                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            Text("${(porcentaje * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = Color(0xFF4B6CB7), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
