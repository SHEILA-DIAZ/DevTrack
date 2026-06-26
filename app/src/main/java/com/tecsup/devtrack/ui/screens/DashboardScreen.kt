package com.tecsup.devtrack.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tecsup.devtrack.model.Proyecto
import com.tecsup.devtrack.model.Tarea
import com.tecsup.devtrack.navigation.Routes
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    proyectos: List<Proyecto>,
    tareas: List<Tarea>,
    userName: String,
    userEmail: String,
    onNavegar: (String) -> Unit,
    onAgregarProyecto: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showNotifications by remember { mutableStateOf(false) }

    val devTrackBlue = Color(0xFF4B6CB7)
    val devTrackDark = Color(0xFF0D1B3E)
    val devTrackLightBlue = Color(0xFFF1F4F9)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = devTrackDark,
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
                modifier = Modifier.width(300.dp)
            ) {
                DrawerContent(
                    name = userName,
                    email = userEmail,
                    proyCount = proyectos.size,
                    tareaCount = tareas.size,
                    onNavegar = onNavegar,
                    onAgregarProyecto = onAgregarProyecto,
                    onLogout = onLogout,
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(32.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = devTrackBlue
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("D", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("DevTrack", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = devTrackDark)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = devTrackDark)
                        }
                    },
                    actions = {
                        BadgedBox(
                            badge = { 
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) { Text("2") } 
                            },
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            IconButton(onClick = { showNotifications = true }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = devTrackDark)
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
                    .background(devTrackLightBlue)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Tarjeta Principal (Bienvenida Dinámica)
                MainWelcomeCard(userName.substringBefore(" "), proyectos.size)

                Spacer(modifier = Modifier.height(24.dp))

                // Estadísticas Rápidas
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard(
                        title = "Proyectos",
                        value = proyectos.size.toString(),
                        subtitle = "${proyectos.count { it.estado == "En desarrollo" }} en progreso",
                        icon = Icons.Default.Info,
                        iconColor = Color(0xFF4B6CB7),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Tareas totales",
                        value = tareas.size.toString(),
                        subtitle = "${tareas.count { it.estado == "Finalizada" || it.estado == "Completada" }} completadas",
                        icon = Icons.Default.CheckCircle,
                        iconColor = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Estado de Proyectos (Donut Chart)
                ProjectStatusChart(proyectos)

                Spacer(modifier = Modifier.height(32.dp))

                // Proyectos Recientes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Proyectos recientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = devTrackDark)
                    TextButton(onClick = { onNavegar(Routes.LISTA_PROYECTOS) }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Ver todos", color = devTrackBlue, fontWeight = FontWeight.Bold)
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp), tint = devTrackBlue)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (proyectos.isEmpty()) {
                    EmptyPlaceholder()
                } else {
                    proyectos.take(3).forEach { proyecto ->
                        RecentProjectCard(proyecto, onNavegar)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (showNotifications) {
                ModalBottomSheet(
                    onDismissRequest = { showNotifications = false },
                    sheetState = sheetState,
                    containerColor = Color.White,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    NotificationPanel()
                }
            }
        }
    }
}

@Composable
fun MainWelcomeCard(firstName: String, activeProjects: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1B3E))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Ilustración Tecnológica Local (Compose)
            TechIllustration(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(160.dp)
            )

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Buenos días, 👋", color = Color.White, fontSize = 16.sp)
                Text(firstName, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale("es", "ES"))
                Text("$day $month", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                Text("$activeProjects proyectos activos", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun TechIllustration(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Laptop base
        Surface(
            modifier = Modifier.size(110.dp, 65.dp).offset(y = 20.dp),
            color = Color(0xFF4B6CB7).copy(alpha = 0.25f),
            shape = RoundedCornerShape(6.dp)
        ) {}
        // Screen
        Surface(
            modifier = Modifier.size(90.dp, 60.dp),
            color = Color(0xFF1A237E),
            shape = RoundedCornerShape(4.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // Header of the "app"
                Row(modifier = Modifier.fillMaxWidth().height(10.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(Modifier.weight(1f).fillMaxHeight().background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(2.dp)))
                    Box(Modifier.weight(2f).fillMaxHeight().background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp)))
                }
                // Main graph area
                Box(Modifier.fillMaxWidth().height(25.dp).background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(2.dp))) {
                    Row(Modifier.fillMaxSize().padding(2.dp), horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.Bottom) {
                        repeat(5) { i ->
                            Box(Modifier.weight(1f).fillMaxHeight(0.3f + (i * 0.15f)).background(Color(0xFF4B6CB7).copy(alpha = 0.8f), RoundedCornerShape(1.dp)))
                        }
                    }
                }
                // Small icons
                Row(modifier = Modifier.fillMaxWidth().height(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) {
                        Box(Modifier.size(8.dp).background(Color(0xFFF9A825).copy(alpha = 0.6f), CircleShape))
                    }
                }
            }
        }
        // Extra floating "KPI"
        Surface(
            modifier = Modifier.size(35.dp, 35.dp).offset(x = 45.dp, y = (-25).dp),
            color = Color(0xFF2E7D32).copy(alpha = 0.9f),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, subtitle: String, icon: ImageVector, iconColor: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(value, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B3E))
            Text(subtitle, color = iconColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ProjectStatusChart(proyectos: List<Proyecto>) {
    val total = proyectos.size
    val finalizados = proyectos.count { it.estado == "Finalizado" }
    val enProgreso = proyectos.count { it.estado == "En desarrollo" }
    val pendientes = proyectos.count { it.estado == "Planificado" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Estado de proyectos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0D1B3E))
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                    if (total > 0) {
                        val fAngle = (finalizados.toFloat() / total) * 360f
                        val pAngle = (enProgreso.toFloat() / total) * 360f
                        val peAngle = (pendientes.toFloat() / total) * 360f

                        Canvas(modifier = Modifier.size(120.dp)) {
                            drawArc(
                                color = Color(0xFF2E7D32),
                                startAngle = -90f,
                                sweepAngle = fAngle,
                                useCenter = false,
                                style = Stroke(width = 25.dp.toPx(), cap = StrokeCap.Butt)
                            )
                            drawArc(
                                color = Color(0xFF1976D2),
                                startAngle = -90f + fAngle,
                                sweepAngle = pAngle,
                                useCenter = false,
                                style = Stroke(width = 25.dp.toPx(), cap = StrokeCap.Butt)
                            )
                            drawArc(
                                color = Color(0xFFF9A825),
                                startAngle = -90f + fAngle + pAngle,
                                sweepAngle = peAngle,
                                useCenter = false,
                                style = Stroke(width = 25.dp.toPx(), cap = StrokeCap.Butt)
                            )
                        }
                    } else {
                        CircularProgressIndicator(modifier = Modifier.size(80.dp), color = Color.LightGray, strokeWidth = 8.dp)
                    }
                }
                Spacer(modifier = Modifier.width(32.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ChartLegendItem(Color(0xFF2E7D32), "Finalizados", if (total > 0) "${(finalizados * 100 / (total.takeIf { it > 0 } ?: 1))}%" else "0%")
                    ChartLegendItem(Color(0xFF1976D2), "En Progreso", if (total > 0) "${(enProgreso * 100 / (total.takeIf { it > 0 } ?: 1))}%" else "0%")
                    ChartLegendItem(Color(0xFFF9A825), "Planificados", if (total > 0) "${(pendientes * 100 / (total.takeIf { it > 0 } ?: 1))}%" else "0%")
                }
            }
        }
    }
}

@Composable
fun ChartLegendItem(color: Color, label: String, percentage: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, modifier = Modifier.weight(1f), fontSize = 14.sp, color = Color.Gray)
        Text(percentage, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0D1B3E))
    }
}

@Composable
fun RecentProjectCard(proyecto: Proyecto, onNavegar: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavegar(Routes.tareas(proyecto.id)) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF4B6CB7).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF4B6CB7))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(proyecto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                    Surface(color = Color(0xFFE3F2FD), shape = CircleShape) {
                        Text(
                            text = "• ${proyecto.estado}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = Color(0xFF1976D2),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(proyecto.descripcion, color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    proyecto.tecnologias.split(",").take(2).filter { it.isNotBlank() }.forEach { tech ->
                        Surface(color = Color(0xFFF1F3F4), shape = RoundedCornerShape(8.dp)) {
                            Text(tech.trim(), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                val progreso = if (proyecto.estado == "Finalizado") 1f else if (proyecto.estado == "En desarrollo") 0.6f else 0.25f
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.width(60.dp).height(6.dp).clip(CircleShape),
                    color = Color(0xFF4B6CB7),
                    trackColor = Color(0xFFF1F3F4)
                )
                Text("${(progreso * 100).toInt()}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B3E))
            }
        }
    }
}

@Composable
fun NotificationPanel() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text("Notificaciones", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0D1B3E))
        Spacer(modifier = Modifier.height(24.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            NotificationItem("Nueva Tarea", "Se ha asignado una nueva tarea al proyecto DevTrack.", "Hace 10 min")
            NotificationItem("Fecha Límite", "El proyecto 'App Fintech' vence en 2 días.", "Hace 1 hora")
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun NotificationItem(title: String, body: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Surface(modifier = Modifier.size(10.dp), shape = CircleShape, color = Color(0xFF4B6CB7)) {}
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0D1B3E))
                Text(body, fontSize = 13.sp, color = Color.Gray)
                Text(time, fontSize = 11.sp, color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun DrawerContent(
    name: String, 
    email: String, 
    proyCount: Int, 
    tareaCount: Int, 
    onNavegar: (String) -> Unit, 
    onAgregarProyecto: () -> Unit,
    onLogout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val initials = if(name.isNotBlank()) {
        name.split(" ").filter { it.isNotBlank() }.map { it.first().uppercase() }.take(2).joinToString("")
    } else "DT"
    
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(64.dp), 
                shape = CircleShape, 
                color = Color.White.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(email, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DrawerStatCard("$proyCount", "Proyectos", Modifier.weight(1f))
            DrawerStatCard("$tareaCount", "Tareas activas", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DrawerLink(Icons.Default.Home, "Dashboard") { 
                onCloseDrawer()
                onNavegar(Routes.DASHBOARD)
            }
            DrawerLink(Icons.Default.Add, "Nuevo Proyecto") { 
                onCloseDrawer()
                onAgregarProyecto()
            }
            DrawerLink(Icons.Default.Info, "Mis Proyectos") { 
                onCloseDrawer()
                onNavegar(Routes.LISTA_PROYECTOS)
            }
            DrawerLink(Icons.Default.CheckCircle, "Tareas") { 
                onCloseDrawer()
                onNavegar(Routes.tareas(0))
            }
            DrawerLink(Icons.Default.Info, "Recursos Tecnológicos") { 
                onCloseDrawer()
                onNavegar(Routes.RECURSOS)
            }
            DrawerLink(Icons.Default.Person, "Mi Perfil") { 
                onCloseDrawer()
                onNavegar(Routes.PROFILE)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLogout(); onCloseDrawer() }
                .clip(RoundedCornerShape(12.dp)),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFFF5252))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Cerrar Sesión", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun DrawerStatCard(value: String, label: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.08f)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }
    }
}

@Composable
fun DrawerLink(icon: ImageVector, label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.clip(RoundedCornerShape(12.dp)),
        color = Color.Transparent
    ) {
        Row(modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
fun EmptyPlaceholder() {
    Card(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("No hay proyectos recientes", color = Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}
