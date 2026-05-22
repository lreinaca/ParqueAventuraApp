package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel

@Composable
fun PantallaDetalleAtraccion(
    navController: NavController,
    atraccionViewModel: AtraccionViewModel,
    atraccionId: String
) {
    val atracciones by atraccionViewModel.atracciones.collectAsState()
    val atraccion = atracciones.firstOrNull { it.id == atraccionId }

    if (atraccion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = VerdeAccent)
        }
        return
    }

    // OBTENEMOS LOS DATOS DE UI PROCESADOS DESDE EL VIEWMODEL
    val uiState = atraccionViewModel.obtenerDetalleUI(atraccion)

    val favoritos by atraccionViewModel.favoritos.collectAsState()
    val esFavorito = favoritos.contains(atraccion.id)

    Column(
        modifier = Modifier.fillMaxSize().background(FondoPantalla).verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            AsyncImage(model = atraccion.imagen, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 40.dp, start = 16.dp).background(Color.White.copy(alpha = 0.85f), CircleShape)
            ) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver") }
            IconButton(
                onClick = { atraccionViewModel.toggleFavorito(atraccion.id) },
                modifier = Modifier
                    .padding(top = 40.dp, end = 16.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.White.copy(alpha = 0.85f), CircleShape)
            ) {
                Icon(
                    imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    tint = if (esFavorito) Color.Red else Color.Black,
                    contentDescription = if (esFavorito) "Eliminar de favoritos" else "Agregar a favoritos"
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).padding(20.dp)) {
            Text(text = atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextoPrincipal)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = uiState.estadoColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = uiState.estadoBadge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = uiState.estadoColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = VerdeAccent.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = atraccion.tipo.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = VerdeAccent,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = FondoPantalla)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Intensidad", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(uiState.nivelIntensidad, fontWeight = FontWeight.Bold, color = uiState.color)
                    }
                    LinearProgressIndicator(
                        progress = uiState.valorProgreso,
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = uiState.color,
                        strokeCap = StrokeCap.Round
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = TextoPrincipal)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "${atraccion.tiempoEspera} min espera", fontSize = 13.sp, color = TextoSecundario)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = VerdeAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = uiState.zonaParque, fontSize = 14.sp, color = TextoSecundario)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Descripción", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                text = "Atracción de tipo ${atraccion.tipo} con una duración de ${atraccion.duracion} min. Tiempo de espera estimado: ${atraccion.tiempoEspera} minutos. Estado actual: ${uiState.estadoBadge}.",
                color = TextoSecundario
            )

            Spacer(modifier = Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = FondoPantalla)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = VerdeAccent)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Ubicación en el parque", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = uiState.zonaParque, color = TextoSecundario, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
