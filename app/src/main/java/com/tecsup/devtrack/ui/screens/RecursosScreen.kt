package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tecsup.devtrack.viewmodel.RecursosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecursosScreen(
    viewModel: RecursosViewModel,
    onVolver: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val devTrackBlue = Color(0xFF4B6CB7)
    val devTrackDark = Color(0xFF0D1B3E)
    val lightGray = Color(0xFFF1F4F9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recursos Tecnológicos", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = devTrackBlue)
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
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Frase Motivacional
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = uiState.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.7f))
                                )
                            )
                    )
                    
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.3f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }

                        Column {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    text = "\"${uiState.frase}\"",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 24.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(modifier = Modifier.size(32.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.2f)) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(uiState.autor.take(if(uiState.autor.length >= 2) 2 else uiState.autor.length).uppercase(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(uiState.autor, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        Text("Autor tecnológico", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                    
                    IconButton(
                        onClick = { viewModel.cargarRecurso() },
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tip del día
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = devTrackBlue.copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = devTrackBlue)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Tip del día", fontWeight = FontWeight.Bold, color = devTrackDark)
                        Text(
                            "Las frases motivacionales ayudan a mantener el enfoque durante el desarrollo de proyectos tecnológicos. Leer una idea inspiradora puede mejorar la productividad, la creatividad y la constancia del equipo.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Recursos recomendados", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = devTrackDark)
                Text("6 recursos", color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grid de Recursos Recomendados (Simulado)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RecursoRecomendadoCard(Icons.Default.List, "Clean Architecture", "Libro", Modifier.weight(1f))
                    RecursoRecomendadoCard(Icons.Default.Build, "Docker + Kubernetes", "Curso", Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RecursoRecomendadoCard(Icons.Default.Search, "System Design Guide", "Artículo", Modifier.weight(1f))
                    RecursoRecomendadoCard(Icons.Default.Place, "TypeScript Handbook", "Doc", Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RecursoRecomendadoCard(icon: ImageVector, title: String, type: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Icon(icon, contentDescription = null, tint = Color(0xFF4B6CB7), modifier = Modifier.size(24.dp))
                Icon(Icons.Default.Star, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0D1B3E), maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(color = Color(0xFFF1F4F9), shape = RoundedCornerShape(4.dp)) {
                Text(type, color = Color(0xFF4B6CB7), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
        }
    }
}
