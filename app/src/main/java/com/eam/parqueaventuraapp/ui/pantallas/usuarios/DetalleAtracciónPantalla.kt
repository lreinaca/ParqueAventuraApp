package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

    Column(
        modifier = Modifier.fillMaxSize().background(FondoPantalla).verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            AsyncImage(model = atraccion.imagen, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 40.dp, start = 16.dp).background(Color.White.copy(alpha = 0.85f), CircleShape)
            ) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver") }
        }

        Column(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).padding(20.dp)) {
            Text(text = atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextoPrincipal)
            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta de Intensidad (Usando datos del ViewModel)
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = FondoPantalla)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Intensidad", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(uiState.nivelIntensidad, fontWeight = FontWeight.Bold, color = uiState.color)
                    }
                    LinearProgressIndicator(
                        progress = { uiState.valorProgreso },
                        modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 8.dp).clip(RoundedCornerShape(4.dp)),
                        color = uiState.color,
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ubicación (Usando datos del ViewModel)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = VerdeAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = uiState.zonaParque, fontSize = 14.sp, color = TextoSecundario)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Descripción", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Atracción ${atraccion.tipo} con duración de ${atraccion.duracion} min.", color = TextoSecundario)
        }
    }
}
