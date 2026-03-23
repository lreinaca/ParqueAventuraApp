package com.eam.parqueaventuraapp.ui.pantallas

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.eam.parqueaventuraapp.ui.theme.AccentGreen
import com.eam.parqueaventuraapp.ui.theme.LightGrayText
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun PantallaMiPerfil(navController: NavController, viewModel: UsuarioViewModel) {
    // Observamos el usuario actual desde el ViewModel para mostrar su información real
    val usuario by viewModel.usuarioActual.observeAsState()
    val scrollState = rememberScrollState()

    // Usamos Scaffold para añadir la barra de navegación inferior fácilmente
    Scaffold(
        bottomBar = {
            BarraNavegacionInferior()
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
                EstadisticaCard(icon = Icons.Default.FavoriteBorder, value = "1", label = "Favoritos", iconColor = Color.Red)
                EstadisticaCard(icon = Icons.Default.StarBorder, value = "4.9", label = "Rating", iconColor = Color(0xFFFFB300))
                EstadisticaCard(icon = Icons.Default.Settings, value = "6", label = "Activas", iconColor = AccentGreen)
            }

            // Menú de opciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    OpcionPerfilItem(icon = Icons.Default.FavoriteBorder, iconBgColor = Color(0xFFFFEBEE), iconColor = Color.Red, title = "Mis Favoritos", subtitle = "1 atracciones")
                    OpcionPerfilItem(icon = Icons.Default.NotificationsNone, iconBgColor = Color(0xFFFFF3E0), iconColor = Color(0xFFFF9800), title = "Notificaciones", subtitle = "Gestionar alertas")
                    OpcionPerfilItem(icon = Icons.Outlined.Settings, iconBgColor = Color(0xFFF5F5F5), iconColor = Color.Gray, title = "Configuración", subtitle = "Preferencias de la app")
                    OpcionPerfilItem(icon = Icons.AutoMirrored.Filled.HelpOutline, iconBgColor = Color(0xFFE3F2FD), iconColor = Color(0xFF2196F3), title = "Ayuda", subtitle = "Centro de soporte", isLast = true)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Cerrar Sesión
            Button(
                onClick = { 
                    navController.navigate("login") { popUpTo(0) }
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
}

@Composable
fun BarraNavegacionInferior() {
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
            ItemNavegacion(icon = Icons.Default.Home, label = "Inicio")
            ItemNavegacion(icon = Icons.Default.ConfirmationNumber, label = "Atracciones")
            ItemNavegacion(icon = Icons.Default.FavoriteBorder, label = "Favoritos")
            ItemNavegacion(icon = Icons.Default.Map, label = "Mapa")
            // El item seleccionado (Perfil) tiene fondo verde claro y color verde
            ItemNavegacion(icon = Icons.Default.Person, label = "Perfil", isSelected = true)
        }
    }
}

@Composable
fun ItemNavegacion(icon: ImageVector, label: String, isSelected: Boolean = false) {
    // Cada icono individual de la barra inferior
    val color = if (isSelected) AccentGreen else Color.Gray
    val bgColor = if (isSelected) Color(0xFFE8F5E9) else Color.Transparent

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
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
fun OpcionPerfilItem(icon: ImageVector, iconBgColor: Color, iconColor: Color, title: String, subtitle: String, isLast: Boolean = false) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable {}.padding(16.dp),
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
