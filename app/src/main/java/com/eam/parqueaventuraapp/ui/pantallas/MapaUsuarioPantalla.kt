package com.eam.parqueaventuraapp.ui.pantallas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.ui.theme.AccentGreen

@Composable
fun PantallaMapaUsuario(navController: NavController) {
    Scaffold(
        bottomBar = {
            BarraNavegacionMapa()
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
                color = Color.Black
            )
            Text(
                text = "Toca un marcador para ver detalles",
                fontSize = 14.sp,
                color = Color.Gray
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
                    
                    // Línea vertical central
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(size.width / 2, 50f),
                        end = Offset(size.width / 2, size.height - 50f),
                        pathEffect = pathEffect
                    )
                    
                    // Línea horizontal central
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(50f, size.height / 3),
                        end = Offset(size.width - 50f, size.height / 3),
                        pathEffect = pathEffect
                    )

                    // Silueta verde del parque (Simulada con un arco o línea)
                    drawCircle(
                        color = AccentGreen.copy(alpha = 0.1f),
                        radius = 400f,
                        center = Offset(size.width / 2.5f, size.height / 4f)
                    )
                }

                // --- MARCADORES Y ETIQUETAS (Posiciones aproximadas a la imagen) ---

                // Zona Extrema (Arriba Derecha)
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp, start = 120.dp),
                    nombre = "Montaña Rusa E",
                    color = Color(0xFFEF5350),
                    zona = "Zona Extrema"
                )

                // Mundo Infantil (Centro)
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.Center).padding(bottom = 60.dp, start = 40.dp),
                    nombre = "Mundo Infantil",
                    color = Color(0xFFFF9800)
                )

                // Carrusel Mágico (Centro Izquierda)
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 60.dp, bottom = 20.dp),
                    nombre = "Carrusel Mágico",
                    color = Color(0xFFFF9800)
                )

                // Rio Salvaje (Abajo Centro)
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.Center).padding(top = 180.dp),
                    nombre = "Rio Salvaje",
                    color = Color(0xFF42A5F5),
                    zona = "Zona Familiar"
                )

                // Noria Panorámica (Abajo Derecha)
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 220.dp, end = 50.dp),
                    nombre = "Noria Panorámica",
                    color = Color(0xFF42A5F5)
                )

                // Tirolesa Aventura (Abajo Izquierda)
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 150.dp, start = 40.dp),
                    nombre = "Tirolesa Aventura",
                    color = Color(0xFFEF5350)
                )

                // Casa del Terror (Muy abajo)
                MarcadorMapa(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 60.dp, end = 60.dp),
                    nombre = "Casa del Terror",
                    color = Color(0xFFEF5350),
                    zona = "Zona Infantil" // Etiqueta naranja cerca
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
    zona: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Etiqueta de Zona (Pill) opcional
        if (zona != null) {
            Surface(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = zona,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        // Icono de Marcador con efecto de elevación
        Surface(
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Nombre del lugar
        Text(
            text = nombre,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun BarraNavegacionMapa() {
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
            ItemMenuMapa(icon = Icons.Default.Home, label = "Inicio")
            ItemMenuMapa(icon = Icons.Default.ConfirmationNumber, label = "Atracciones")
            ItemMenuMapa(icon = Icons.Default.FavoriteBorder, label = "Favoritos")
            ItemMenuMapa(icon = Icons.Default.Map, label = "Mapa", isSelected = true)
            ItemMenuMapa(icon = Icons.Default.Person, label = "Perfil")
        }
    }
}

@Composable
fun ItemMenuMapa(icon: ImageVector, label: String, isSelected: Boolean = false) {
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
