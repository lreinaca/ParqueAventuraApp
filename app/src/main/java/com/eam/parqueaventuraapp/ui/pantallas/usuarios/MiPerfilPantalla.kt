package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.data.modelo.Roles
import com.eam.parqueaventuraapp.ui.theme.AccentGreen
import com.eam.parqueaventuraapp.ui.theme.TextoPrincipal
import com.eam.parqueaventuraapp.ui.theme.TextoSecundario
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun PantallaMiPerfil(navController: NavController, viewModel: UsuarioViewModel, atraccionViewModel: com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel) {
    // Observamos el usuario actual desde el ViewModel para mostrar su información real
    val usuario by viewModel.usuarioActual.collectAsState()
    val favoritosSet by atraccionViewModel.favoritos.collectAsState()
    val atracciones by atraccionViewModel.atracciones.collectAsState()
    val favoritosCount = favoritosSet.size
    val activasCount = atracciones.count { it.estado.equals("ABIERTA", ignoreCase = true) }
    var showHelp by remember { mutableStateOf(false) }
    var showConfig by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Usamos Scaffold para añadir la barra de navegación inferior fácilmente
    Scaffold(
        bottomBar = {
            // Pasamos el navController a la barra inferior
            BarraNavegacionInferior(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)) // Fondo gris suave
                .verticalScroll(scrollState)
                .padding(innerPadding) // Aplica el padding del Scaffold
                .padding(16.dp)
        ) {
            // Título de la pantalla
            Text(
                text = "Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Cabecera: Foto de perfil (inicial) y datos del usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(AccentGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = usuario?.nombre?.firstOrNull()?.toString()?.uppercase() ?: "U",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = usuario?.nombre ?: "Usuario Demo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = usuario?.correo ?: "demo@aventurapark.com",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Visitante",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            // Fila de tarjetas de estadísticas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EstadisticaCard(icon = Icons.Default.FavoriteBorder, value = favoritosCount.toString(), label = "Favoritos", iconColor = Color.Red)
                EstadisticaCard(icon = Icons.Default.Settings, value = activasCount.toString(), label = "Activas", iconColor = AccentGreen)
            }

            // Menú de opciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    OpcionPerfilItem(
                        icon = Icons.Default.FavoriteBorder,
                        iconBgColor = Color(0xFFFFEBEE),
                        iconColor = Color.Red,
                        title = "Mis Favoritos",
                        subtitle = "${favoritosCount} atracciones",
                        onClick = { navController.navigate("favoritos") }
                    )
                    OpcionPerfilItem(
                        icon = Icons.Default.NotificationsNone,
                        iconBgColor = Color(0xFFFFF3E0),
                        iconColor = Color(0xFFFF9800),
                        title = "Notificaciones",
                        subtitle = "Gestionar alertas",
                        onClick = { showNotifications = true })

                    OpcionPerfilItem(
                        icon = Icons.Outlined.Settings,
                        iconBgColor = Color(0xFFF5F5F5),
                        iconColor = Color.Gray,
                        title = "Configuración",
                        subtitle = "Preferencias de la app",
                        onClick = { showConfig = true })

                    OpcionPerfilItem(
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        iconBgColor = Color(0xFFE3F2FD),
                        iconColor = Color(0xFF2196F3),
                        title = "Ayuda",
                        subtitle = "Centro de soporte",
                        isLast = true,
                        onClick = { showHelp = true })
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Cerrar Sesión
            Button(
                onClick = {
                    viewModel.cerrarSesion()
                    navController.navigate("login") { 
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), contentColor = Color.Red)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Cerrar Sesión", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Aventura Park v1.0.0",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

        // Dialogos simples para Ayuda / Configuración / Notificaciones
        if (showHelp) {
            AlertDialog(
                onDismissRequest = { showHelp = false },
                title = { Text("Ayuda & Soporte", color = TextoPrincipal) },
                text = {
                    Column {
                        Text("Si tienes dudas, contáctanos:", color = TextoSecundario)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Correo: admin@parqueaventura.com", color = TextoPrincipal)
                        Text("Tel: 3114318088", color = TextoPrincipal)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Horario de atención: Lun-Vie 9:00 - 18:00", color = TextoSecundario)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showHelp = false }) { Text("Volver", color = AccentGreen) }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }

        if (showConfig) {
            AlertDialog(
                onDismissRequest = { showConfig = false },
                title = { Text("Configuración", color = TextoPrincipal) },
                text = {
                    Column {
                        Text("Ajustes de la aplicación:", color = TextoSecundario)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("- Tema: Claro", color = TextoPrincipal)
                        Text("- Idioma: Español", color = TextoPrincipal)
                        Text("- Bloqueo por PIN: Desactivado", color = TextoPrincipal)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showConfig = false }) { Text("Cerrar", color = AccentGreen) }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }

        if (showNotifications) {
            AlertDialog(
                onDismissRequest = { showNotifications = false },
                title = { Text("Notificaciones", color = TextoPrincipal) },
                text = { Text("No tienes notificaciones nuevas.", color = TextoSecundario) },
                confirmButton = {
                    TextButton(onClick = { showNotifications = false }) { Text("Cerrar", color = AccentGreen) }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
}

@Composable
fun BarraNavegacionInferior(navController: NavController) {
    // Fila que simula la barra de navegación inferior de la imagen
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemNavegacionPerfil(
                icon = Icons.Default.Home, 
                label = "Inicio",
                onClick = { navController.navigate("inicioUsuario") }
            )

            // Configuración para ir a la pantalla de atracciones
            ItemNavegacionPerfil(
                icon = Icons.Default.ConfirmationNumber,
                label = "Atracciones",
                onClick = { navController.navigate("atracciones") }
            )
            // Configuración para ir a la pantalla de Favoritos
            ItemNavegacionPerfil(
                icon = Icons.Default.FavoriteBorder,
                label = "Favoritos",
                onClick = { navController.navigate("favoritos") }
            )

            // Configuración para ir a la pantalla Mapa del parque
            ItemNavegacionPerfil(
                icon = Icons.Default.Map, 
                label = "Mapa",
                onClick = { navController.navigate("mapa_parque") }
            )

            // El item seleccionado (Perfil) tiene fondo verde claro y color verde
            ItemNavegacionPerfil(
                icon = Icons.Default.Person, 
                label = "Perfil", 
                isSelected = true,
                onClick = { /* Ya estamos en perfil */ }
            )
        }
    }
}

@Composable
fun ItemNavegacionPerfil(
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {} // Función para manejar el clic en el ítem mediante este parámetro lambda
) {
    // Cada icono individual de la barra inferior
    val color = if (isSelected) AccentGreen else Color.Gray
    val bgColor = if (isSelected) Color(0xFFE8F5E9) else Color.Transparent

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            // hacemos que el item pueda responder al clic y llamamos a la función onClick
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = color,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun EstadisticaCard(icon: ImageVector, value: String, label: String, iconColor: Color) {
    Card(
        modifier = Modifier.width(100.dp).height(110.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = label, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
fun OpcionPerfilItem(
    icon: ImageVector, 
    iconBgColor: Color, 
    iconColor: Color, 
    title: String, 
    subtitle: String, 
    isLast: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(40.dp).background(iconBgColor, CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = subtitle, color = Color.Gray, fontSize = 12.sp)
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
        if (!isLast) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
        }
    }
}
