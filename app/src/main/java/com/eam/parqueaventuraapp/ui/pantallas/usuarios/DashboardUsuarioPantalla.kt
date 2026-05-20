package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.ui.componentes.BarraNavegacionInferior
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun PantallaInicio(navController: NavController, usuarioViewModel: UsuarioViewModel, atraccionViewModel: AtraccionViewModel) {
    val usuario by usuarioViewModel.usuarioActual.collectAsState()
    
    // Se corrige el nombre de la propiedad a uiStateUsuario y se usa el estado procesado
    val uiState by atraccionViewModel.uiStateUsuario.collectAsState()

    Scaffold(
        bottomBar = { BarraNavegacionInferior(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(FondoPantalla),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
        ) {
            item {
                CabeceraHome(
                    nombreUsuario = usuario?.nombre ?: "Usuario",
                    totalAtracciones = uiState.totalAtracciones,
                    disponibles = uiState.disponibles
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Atracciones disponibles", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextoPrincipal)
                    TextButton(onClick = { navController.navigate("atracciones") }) {
                        Text(text = "Ver todas >", color = VerdeAccent, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Se usa la lista que ya viene filtrada desde el ViewModel
            items(uiState.listaAtracciones) { atraccion ->
                ItemAtraccion(
                    atraccion = atraccion,
                    onVerDetalle = { navController.navigate("detalle/${atraccion.id}") }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun CabeceraHome(nombreUsuario: String, totalAtracciones: Int, disponibles: Int) {
    Box(modifier = Modifier.fillMaxWidth().background(VerdeOscuro, shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)).padding(20.dp)) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(50.dp).background(VerdeAccent, CircleShape), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = "https://res.cloudinary.com/djn8thk2s/image/upload/v1775453956/Logo_de_Parque_Aventura_xqo823.png",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Aventura Park", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Icon(Icons.Default.Notifications, null, tint = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("¡Hola!", color = VerdeAccent, fontWeight = FontWeight.SemiBold)
            Text(nombreUsuario, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            Text("Prepárate para vivir un día lleno de aventuras", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                TarjetaContador("Atracciones", totalAtracciones.toString(), Modifier.weight(1f))
                Spacer(modifier = Modifier.width(10.dp))
                TarjetaContador("Disponibles", disponibles.toString(), Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TarjetaContador(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)).padding(16.dp)) {
        Column {
            Text(valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(titulo, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

@Composable
fun ItemAtraccion(atraccion: Atraccion, onVerDetalle: () -> Unit) {
    Card(onClick = onVerDetalle, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.height(100.dp)) {
            AsyncImage(model = atraccion.imagen, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.width(100.dp).fillMaxHeight().clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)))
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextoPrincipal)
                    BadgeCategoria(atraccion.tipo)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BadgeTiempo(atraccion.tiempoEspera)
                    Spacer(modifier = Modifier.width(8.dp))
                    BadgeEstadoA(atraccion.estado)
                }
            }
        }
    }
}

@Composable
fun BadgeCategoria(tipo: String) {
    val color = when (tipo.lowercase()) {
        "infantil" -> ColorInfantil
        "extrema" -> ColorExtrema
        "familiar" -> ColorFamiliar
        else -> Color.Gray
    }
    Box(modifier = Modifier.background(color, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 3.dp)) {
        Text(tipo.replaceFirstChar { it.uppercase() }, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BadgeTiempo(minutos: Int) {
    Row(modifier = Modifier.background(Color(0xFFE8F5E9), RoundedCornerShape(20.dp)).padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("⏱", fontSize = 11.sp)
        Text("$minutos min", color = VerdeClaro, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun BadgeEstadoA(estado: String) {
    val (texto, fondo, colorTexto) = when (estado.uppercase()) {
        "ABIERTA" -> Triple("Activa", Color(0xFFE8F5E9), VerdeClaro)
        "CERRADA" -> Triple("Cerrada", Color(0xFFFFEBEE), Color(0xFFC62828))
        "MANTENIMIENTO" -> Triple("Mantenimiento", Color(0xFFFFF8E1), Color(0xFFF57F17))
        else -> Triple(estado, Color(0xFFF5F5F5), Color.Gray)
    }
    Box(modifier = Modifier.background(fondo, RoundedCornerShape(20.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(texto, color = colorTexto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
