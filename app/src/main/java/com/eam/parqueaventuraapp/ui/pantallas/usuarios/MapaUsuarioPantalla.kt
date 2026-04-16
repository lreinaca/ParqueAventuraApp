package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.eam.parqueaventuraapp.ui.theme.AccentGreen
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapaUsuario(navController: NavController, atraccionViewModel: AtraccionViewModel) {
    var selectedAtraccion by remember { mutableStateOf<Atraccion?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val atracciones by atraccionViewModel.atracciones.collectAsState()

    Scaffold(
        bottomBar = {
            BarraNavegacionMapa(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Cabecera del Mapa
            Text(
                text = "Mapa del Parque",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Text(
                text = "Toca un marcador para ver detalles",
                fontSize = 14.sp,
                color = TextoSecundario
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Área del Mapa Interactiva (Simulada)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF8F9FA))
            ) {
                // Dibujo de líneas punteadas de fondo
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                    drawLine(
                        color = Color.LightGray,
                        start = Offset(size.width / 2, 50f),
                        end = Offset(size.width / 2, size.height - 50f),
                        pathEffect = pathEffect
                    )

                    drawLine(
                        color = Color.LightGray,
                        start = Offset(50f, size.height / 3),
                        end = Offset(size.width - 50f, size.height / 3),
                        pathEffect = pathEffect
                    )

                    drawCircle(
                        color = AccentGreen.copy(alpha = 0.1f),
                        radius = 400f,
                        center = Offset(size.width / 2.5f, size.height / 4f)
                    )
                }

                // --- MARCADORES (Coregidos con onClick) ---
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp, start = 120.dp),
                    nombre = "Montaña Rusa E",
                    color = Color(0xFFEF5350),
                    zona = "Zona Extrema",
                    onClick = { selectedAtraccion = atracciones.find { it.nombre.contains("Montaña") } }
                )

                MarcadorMapa(
                    modifier = Modifier.align(Alignment.Center).padding(bottom = 60.dp, start = 40.dp),
                    nombre = "Mundo Infantil",
                    color = Color(0xFFFF9800),
                    onClick = { selectedAtraccion = atracciones.find { it.nombre.contains("Infantil") } }
                )

                MarcadorMapa(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 60.dp, bottom = 20.dp),
                    nombre = "Carrusel Mágico",
                    color = Color(0xFFFF9800),
                    onClick = { selectedAtraccion = atracciones.find { it.nombre.contains("Carrusel") } }
                )

                MarcadorMapa(
                    modifier = Modifier.align(Alignment.Center).padding(top = 180.dp),
                    nombre = "Rio Salvaje",
                    color = Color(0xFF42A5F5),
                    zona = "Zona Familiar",
                    onClick = { selectedAtraccion = atracciones.find { it.nombre.contains("Rio") } }
                )

                MarcadorMapa(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 220.dp, end = 50.dp),
                    nombre = "Noria Panorámica",
                    color = Color(0xFF42A5F5),
                    onClick = { selectedAtraccion = atracciones.find { it.nombre.contains("Noria") } }
                )

                MarcadorMapa(
                    modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 150.dp, start = 40.dp),
                    nombre = "Tirolesa Aventura",
                    color = Color(0xFFEF5350),
                    onClick = { selectedAtraccion = atracciones.find { it.nombre.contains("Tirolesa") } }
                )

                MarcadorMapa(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 60.dp, end = 60.dp),
                    nombre = "Casa del Terror",
                    color = Color(0xFFEF5350),
                    zona = "Zona Infantil",
                    onClick = { selectedAtraccion = atracciones.find { it.nombre.contains("Casa") } }
                )
            }
        }

        // MODAL (Punto C corregido)
        if (selectedAtraccion != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedAtraccion = null },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
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
fun MarcadorMapa(
    modifier: Modifier = Modifier,
    nombre: String,
    color: Color,
    zona: String? = null,
    onClick: () -> Unit // Parámetro obligatorio
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (zona != null) {
            Surface(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(text = zona, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
        }

        Surface(modifier = Modifier.size(32.dp), shape = CircleShape, color = Color.White, shadowElevation = 4.dp) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
        }

        Text(text = nombre, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal, modifier = Modifier.background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp))
    }
}

@Composable
fun ContenidoModalMapa(atraccion: Atraccion, onVerDetalle: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp, start = 24.dp, end = 24.dp, top = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = atraccion.imagen, contentDescription = null, modifier = Modifier.size(90.dp).clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextoPrincipal)
                Text(text = "Ubicación: ${atraccion.tipo}", fontSize = 14.sp, color = TextoSecundario)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BadgeTiempo(minutos = atraccion.tiempoEspera)
                    BadgeEstado(estado = atraccion.estado)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onVerDetalle, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = VerdeAccent)) {
            Text("Ver más detalles", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun BarraNavegacionMapa(navController: NavController) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 8.dp) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            ItemNavegacionMapa(Icons.Default.Home, label = "Inicio")
            ItemNavegacionMapa(Icons.Default.ConfirmationNumber, label = "Atracciones")
            ItemNavegacionMapa(Icons.Default.FavoriteBorder, label = "Favoritos", onClick = { navController.navigate("favoritos") })
            ItemNavegacionMapa(Icons.Default.Map, label = "Mapa", isSelected = true)
            ItemNavegacionMapa(Icons.Default.Person, label = "Perfil", onClick = { navController.navigate("perfil") })
        }
    }
}

@Composable
fun ItemNavegacionMapa(icon: ImageVector, label: String, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    val color = if (isSelected) AccentGreen else TextoSecundario
    val bgColor = if (isSelected) Color(0xFFE8F5E9) else Color.Transparent
    Column(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(bgColor).clickable { onClick() }.padding(horizontal = 12.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Text(text = label, fontSize = 10.sp, color = color, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}
