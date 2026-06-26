package com.tecsup.devtrack.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.devtrack.viewmodel.ProyectoViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoScreen(
    viewModel: ProyectoViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    // Estado local para el campo de texto de añadir tecnología y sugerencias
    var techInput by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }

    val sugerenciasTech = listOf(
        "Kotlin", "Jetpack Compose", "Firebase", "Room", "Retrofit", 
        "Spring Boot", "Node.js", "React", "Angular", "Docker", 
        "MySQL", "MongoDB", "Python", "TypeScript", "GitHub"
    )

    // Colores DevTrack
    val devTrackBlue = Color(0xFF4B6CB7)
    val devTrackDark = Color(0xFF0D1B3E)
    val lightGray = Color(0xFFF1F4F9)

    // Lógica para DatePicker segura
    val calendar = remember { Calendar.getInstance() }
    
    fun mostrarDatePicker(fechaActual: String, onDateSelected: (String) -> Unit) {
        try {
            val dateParts = if (fechaActual.contains("/")) {
                fechaActual.split("/")
            } else {
                null
            }
            
            val year = dateParts?.getOrNull(2)?.toIntOrNull() ?: calendar.get(Calendar.YEAR)
            val month = (dateParts?.getOrNull(1)?.toIntOrNull() ?: (calendar.get(Calendar.MONTH) + 1)) - 1
            val day = dateParts?.getOrNull(0)?.toIntOrNull() ?: calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                context,
                { _, y, m, d ->
                    val formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y)
                    onDateSelected(formattedDate)
                },
                year, month, day
            ).show()
        } catch (e: Exception) {
            android.util.Log.e("DEVTRACK_ERROR", "Error al mostrar DatePicker", e)
        }
    }

    fun addTechnology(tech: String) {
        try {
            if (tech.isBlank()) return
            val currentTechs = uiState.tecnologias.split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .toMutableList()
            
            if (!currentTechs.any { it.equals(tech.trim(), ignoreCase = true) }) {
                currentTechs.add(tech.trim())
                viewModel.actualizarTecnologias(currentTechs.joinToString(", "))
            }
            techInput = ""
            showSuggestions = false
        } catch (e: Exception) {
            android.util.Log.e("DEVTRACK_ERROR", "Error al añadir tecnología", e)
        }
    }

    fun removeTechnology(techToRemove: String) {
        try {
            val currentTechs = uiState.tecnologias.split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() && !it.equals(techToRemove.trim(), ignoreCase = true) }
            viewModel.actualizarTecnologias(currentTechs.joinToString(", "))
        } catch (e: Exception) {
            android.util.Log.e("DEVTRACK_ERROR", "Error al remover tecnología", e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Proyecto", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = devTrackBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onVolver,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD), contentColor = devTrackBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.guardarProyecto() },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Crear Proyecto", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = devTrackDark)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Build, contentDescription = null, tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Crear nuevo proyecto", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Completa los datos del proyecto", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.mensajeExito.isNotBlank()) {
                SuccessMessage(uiState.mensajeExito)
                Spacer(modifier = Modifier.height(16.dp))
            }

            ProjectLabelText("NOMBRE DEL PROYECTO")
            ProjectTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.actualizarNombre(it) },
                placeholder = "Ej: Sistema de Inventario",
                icon = Icons.Default.Info,
                isError = uiState.mensajeError.contains("nombre")
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProjectLabelText("DESCRIPCIÓN")
            ProjectTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.actualizarDescripcion(it) },
                placeholder = "Describe brevemente el objetivo del proyecto...",
                isError = uiState.mensajeError.contains("descripción"),
                singleLine = false,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProjectLabelText("TECNOLOGÍAS")
            SimpleFlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalGap = 8.dp,
                verticalGap = 8.dp
            ) {
                uiState.tecnologias.split(",").filter { it.isNotBlank() }.forEach { tech ->
                    TechTag(tech.trim(), onRemove = { removeTechnology(tech) })
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = techInput,
                        onValueChange = { techInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Añadir tecnología...", color = Color.LightGray) },
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = devTrackBlue) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedBorderColor = devTrackBlue
                        )
                    )
                    
                    DropdownMenu(
                        expanded = showSuggestions,
                        onDismissRequest = { showSuggestions = false },
                        modifier = Modifier.fillMaxWidth(0.6f).background(Color.White)
                    ) {
                        sugerenciasTech.forEach { tech ->
                            DropdownMenuItem(
                                text = { Text(tech) },
                                onClick = { addTechnology(tech) }
                            )
                        }
                    }
                }
                
                IconButton(
                    onClick = { 
                        if (techInput.isNotBlank()) {
                            addTechnology(techInput)
                        } else {
                            showSuggestions = true
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(devTrackDark, CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    ProjectLabelText("FECHA INICIO")
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    if (isPressed) {
                        LaunchedEffect(Unit) {
                            mostrarDatePicker(uiState.fechaInicio) { viewModel.actualizarFechaInicio(it) }
                        }
                    }
                    ProjectTextField(
                        value = uiState.fechaInicio,
                        onValueChange = { viewModel.actualizarFechaInicio(it) },
                        placeholder = "dd/mm/aaaa",
                        icon = Icons.Default.DateRange,
                        readOnly = false,
                        interactionSource = interactionSource
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    ProjectLabelText("FECHA LÍMITE")
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    if (isPressed) {
                        LaunchedEffect(Unit) {
                            mostrarDatePicker(uiState.fechaLimite) { viewModel.actualizarFechaLimite(it) }
                        }
                    }
                    ProjectTextField(
                        value = uiState.fechaLimite,
                        onValueChange = { viewModel.actualizarFechaLimite(it) },
                        placeholder = "dd/mm/aaaa",
                        icon = Icons.Default.DateRange,
                        readOnly = false,
                        interactionSource = interactionSource
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProjectLabelText("ESTADO DEL PROYECTO")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusSelectorButton("En Desarrollo", uiState.estado == "En desarrollo", Color(0xFF1976D2), Modifier.weight(1f)) {
                    viewModel.actualizarEstado("En desarrollo")
                }
                StatusSelectorButton("Planificado", uiState.estado == "Planificado", Color(0xFFF9A825), Modifier.weight(1f)) {
                    viewModel.actualizarEstado("Planificado")
                }
                StatusSelectorButton("Finalizado", uiState.estado == "Finalizado", Color(0xFF2E7D32), Modifier.weight(1f)) {
                    viewModel.actualizarEstado("Finalizado")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProjectLabelText("OBSERVACIONES")
            var localObservaciones by remember { mutableStateOf("") }
            ProjectTextField(
                value = localObservaciones,
                onValueChange = { localObservaciones = it },
                placeholder = "Escribe observaciones adicionales (opcional)...",
                singleLine = false,
                minLines = 3
            )

            if (uiState.mensajeError.isNotBlank()) {
                Text(text = uiState.mensajeError, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun ProjectLabelText(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0D1B3E),
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
fun ProjectTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = Color.LightGray, fontSize = 14.sp) },
        leadingIcon = icon?.let { { Icon(it, contentDescription = null, tint = Color(0xFF4B6CB7), modifier = Modifier.size(20.dp)) } },
        shape = RoundedCornerShape(12.dp),
        singleLine = singleLine,
        minLines = minLines,
        isError = isError,
        readOnly = readOnly,
        interactionSource = interactionSource,
        // COMENTARIO PARA SUSTENTACIÓN: Se asegura que el campo sea interactuable si es de solo lectura pero tiene un modifier (como un clickable)
        enabled = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedBorderColor = Color(0xFF4B6CB7),
            disabledContainerColor = Color.White,
            disabledBorderColor = Color.White,
            disabledTextColor = Color.Black
        )
    )
}

@Composable
fun TechTag(name: String, onRemove: () -> Unit) {
    Surface(
        color = Color(0xFFF1F4F9),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, color = Color(0xFF4B6CB7), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.Default.Close, 
                contentDescription = null, 
                modifier = Modifier.size(12.dp).clickable { onRemove() }, 
                tint = Color(0xFF4B6CB7)
            )
        }
    }
}

@Composable
fun StatusSelectorButton(text: String, selected: Boolean, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) color else Color.White),
        color = if (selected) color.copy(alpha = 0.05f) else Color.White,
        shadowElevation = if (selected) 0.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = color))
            Text(text, color = if (selected) color else Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SuccessMessage(message: String) {
    Surface(
        color = Color(0xFFE8F5E9),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
            Spacer(modifier = Modifier.width(12.dp))
            Text(message, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
