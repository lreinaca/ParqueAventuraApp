package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapaUsuario(navController: NavController, atraccionViewModel: AtraccionViewModel) {
    var selectedAtraccion by remember { mutableStateOf<Atraccion?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        bottomBar = { BarraNavegacionMapa(navController) }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(innerPadding).padding(16.dp)) {
            Text(text = "Mapa del Parque", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
            Text(text = "Toca un marcador para ver detalles", fontSize = 14.sp, color = TextoSecundario)
            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)).background(Color(0xFFF8F9FA))) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    drawLine(color = Color.LightGray, start = Offset(size.width / 2, 50f), end = Offset(size.width / 2, size.height - 50f), pathEffect = pathEffect)
                    drawCircle(color = VerdeAccent.copy(alpha = 0.1f), radius = 400f, center = Offset(size.width / 2.5f, size.height / 4f))
                }

                // MARCADORES: AHORA USAN LA LÓGICA DEL VIEWMODEL
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp, start = 120.dp),
                    nombre = "Montaña Rusa", color = Color(0xFFEF5350), zona = "Zona Extrema",
                    onClick = { selectedAtraccion = atraccionViewModel.buscarAtraccionPorNombre("Montaña") }
                )

                MarcadorMapa(
                    modifier = Modifier.align(Alignment.Center).padding(bottom = 60.dp, start = 40.dp),
                    nombre = "Infantil", color = Color(0xFFFF9800),
                    onClick = { selectedAtraccion = atraccionViewModel.buscarAtraccionPorNombre("Infantil") }
                )
                
                // ... Otros marcadores simplificados ...
            }
        }

        if (selectedAtraccion != null) {
            ModalBottomSheet(onDismissRequest = { selectedAtraccion = null }, sheetState = sheetState) {
                ContenidoModalMapa(
                    atraccion = selectedAtraccion!!,
                    onVerDetalle = {
                        val id = selectedAtraccion!!.id
                        selectedAtraccion = null
                        navController.navigate("detalle/$id")
                    }
                )
            }
        }
    }
}

@Composable
fun ContenidoModalMapa(atraccion: Atraccion, onVerDetalle: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = atraccion.imagen, contentDescription = null, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Estado: ${atraccion.estado}", fontSize = 13.sp, color = TextoSecundario)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onVerDetalle, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = VerdeAccent)) {
            Text("Ver Detalles", color = Color.White)
        }
    }
}

// ... BarraNavegacionMapa se mantiene igual ...
@Composable
fun BarraNavegacionMapa(navController: NavController) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 8.dp) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = { navController.navigate("inicioUsuario") }) { Icon(Icons.Default.Home, null) }
            IconButton(onClick = { navController.navigate("mapa_parque") }) { Icon(Icons.Default.Map, null, tint = VerdeAccent) }
            IconButton(onClick = { navController.navigate("perfil") }) { Icon(Icons.Default.Person, null) }
        }
    }
}

@Composable
fun MarcadorMapa(modifier: Modifier, nombre: String, color: Color, zona: String? = null, onClick: () -> Unit) {
    Column(modifier = modifier.clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally) {
        if (zona != null) BadgeZona(zona, color)
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = color, modifier = Modifier.size(30.dp))
        Text(text = nombre, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(Color.White, RoundedCornerShape(4.dp)).padding(2.dp))
    }
}

@Composable
fun BadgeZona(texto: String, color: Color) {
    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
        Text(texto, fontSize = 8.sp, color = color, modifier = Modifier.padding(4.dp))
    }
}
