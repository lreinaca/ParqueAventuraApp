package com.eam.parqueaventuraapp.ui.pantallas.admins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun PantallaInicioAdmin(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel,
    atraccionViewModel: AtraccionViewModel
){
    // CONSUMO DE DATOS PROCESADOS DESDE EL VIEWMODEL
    val estadisticas by atraccionViewModel.estadisticasAdmin.collectAsState()
    val usuarios by usuarioViewModel.usuarios.collectAsState()

    // Acceso correcto a las propiedades del data class AdminStats
    val totalAtracciones = estadisticas.total
    val totalUsuarios = usuarios.size
    val activas = estadisticas.activas
    val mantenimientos = estadisticas.mantenimiento
    val cerradas = estadisticas.cerradas
    val recientes = estadisticas.recientes

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(FondoPantalla),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
        ) {
            item {
                CabeceraAdmin(
                    navController = navController,
                    usuarioViewModel = usuarioViewModel,
                    totalAtracciones = totalAtracciones,
                    totalUsuarios = totalUsuarios
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                FilaEstados(activas = activas, mantenimiento = mantenimientos, cerradas = cerradas)
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Acciones rápidas", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(12.dp))
                SeccionAccionesRapidas(navController = navController)
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Atracciones recientes", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(recientes) { atraccion ->
                ItemAtraccionReciente(atraccion = atraccion)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun CabeceraAdmin(navController: NavController, usuarioViewModel: UsuarioViewModel, totalAtracciones: Int, totalUsuarios: Int){
    Box(modifier = Modifier.fillMaxWidth().background(VerdeOscuro, RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)).padding(20.dp)){
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Absolute.spacedBy(12.dp)) {
                Box(modifier = Modifier.size(36.dp).background(Color.White.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
                    IconButton(onClick = {
                        usuarioViewModel.cerrarSesion()
                        navController.navigate("login"){ popUpTo(0) {inclusive = true } }
                    }) { Icon(Icons.Default.ArrowBack, contentDescription =null, tint = Color.White) }
                }
                Column{
                    Text("Panel Administrador", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Parque Aventura", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                TarjetaContadorAdmin(Icons.Default.LocalActivity, totalAtracciones.toString(), "Total\nAtracciones", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(10.dp))
                TarjetaContadorAdmin(Icons.Default.Group, totalUsuarios.toString(), "Usuarios", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TarjetaContadorAdmin(icono: ImageVector, valor: String, titulo: String, modifier: Modifier = Modifier){
    Row (modifier = modifier.background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.size(38.dp).background(VerdeAccent, CircleShape), contentAlignment = Alignment.Center){
            Icon(icono, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Column {
            Text(valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(titulo, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
        }
    }
}

@Composable
fun FilaEstados(activas: Int, mantenimiento: Int, cerradas: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        TarjetaEstado(activas.toString(), "Activas", Color(0xFF4caf50), Modifier.weight(1f))
        TarjetaEstado(mantenimiento.toString(), "Mantenimiento", Color(0xFFff9800), Modifier.weight(1f))
        TarjetaEstado(cerradas.toString(), "Cerradas", Color(0xFF9e9e9e), Modifier.weight(1f))
    }
}

@Composable
fun TarjetaEstado(valor: String, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
            Column {
                Text(valor, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextoPrincipal)
                Text(etiqueta, fontSize = 11.sp, color = TextoSecundario)
            }
        }
    }
}

@Composable
fun SeccionAccionesRapidas(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        BotonAccionRapida(Icons.Default.Add, "Crear\nAtracción", Color(0xFF4CAF50), Modifier.weight(1f)) { navController.navigate("crearAtraccion") }
        BotonAccionRapida(Icons.Default.List, "Gestionar\nAtracciones", Color(0xFF2196F3), Modifier.weight(1f)) { navController.navigate("gestionAtracciones") }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        BotonAccionRapida(Icons.Default.Group, "Gestionar\nUsuarios", Color(0xFFFF6B2B), Modifier.weight(1f)) { navController.navigate("admin_usuarios") }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun BotonAccionRapida(icono: ImageVector, etiqueta: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit){
    Card(onClick = onClick, modifier = modifier.height(100.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.size(36.dp).background(Color.White.copy(alpha = 0.25f), CircleShape), contentAlignment = Alignment.Center){
                Icon(icono, null, tint= Color.White, modifier = Modifier.size(20.dp))
            }
            Text(etiqueta, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun ItemAtraccionReciente(atraccion: Atraccion) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        AsyncImage(model = atraccion.imagen, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(48.dp).clip(CircleShape))
        Column(modifier = Modifier.weight(1f)) {
            Text(atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextoPrincipal)
            Text("${atraccion.tiempoEspera} min de espera", fontSize = 12.sp, color = TextoSecundario)
        }
        BadgeEstado(estado = atraccion.estado)
    }
}

@Composable
fun BadgeEstado(estado: String) {
    val (texto, fondo, colorTexto) = when (estado.uppercase()) {
        "ABIERTA" -> Triple("Activa", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "CERRADA" -> Triple("Cerrada", Color(0xFFFFEBEE), Color(0xFFC62828))
        "MANTENIMIENTO" -> Triple("Mantenimiento", Color(0xFFFFF8E1), Color(0xFFF57F17))
        else -> Triple(estado, Color(0xFFF5F5F5), TextoSecundario)
    }
    Box(modifier = Modifier.background(fondo, RoundedCornerShape(20.dp)).padding(horizontal = 10.dp, vertical = 5.dp)) {
        Text(texto, color = colorTexto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
