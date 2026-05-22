package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.ui.componentes.BarraNavegacionInferior
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel

private const val URL_MAPA = "https://res.cloudinary.com/ddcxvjdno/image/upload/v1779416076/Captura_de_pantalla_2026-05-21_203725_whc1vu.png"

@Composable
fun PantallaMapaUsuario(navController: NavController, atraccionViewModel: AtraccionViewModel) {
    var mostrarMapaCompleto by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BarraNavegacionInferior(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Mapa del Parque",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── MAPA ESTÁTICO ──────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // tamaño pequeño en pantalla
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { mostrarMapaCompleto = true } // toca para agrandar
            ) {
                AsyncImage(
                    model = URL_MAPA,
                    contentDescription = "Mapa del parque — toca para ampliar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Indicador visual de "toca para ampliar"
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.ZoomIn, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Text("Ampliar", fontSize = 11.sp, color = Color.White)
                    }
                }
            }

            Text(
                text = "Toca el mapa para ampliarlo",
                fontSize = 12.sp,
                color = TextoSecundario,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── CONTACTO ───────────────────────────────────
            Text(
                text = "Información de contacto",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilaContacto(Icons.Default.LocationOn, "Dirección", "Km 12 vía Bogotá–Medellín, Cundinamarca")
            FilaContacto(Icons.Default.Phone, "Teléfono", "+57 (1) 345-6789")
            FilaContacto(Icons.Default.Email, "Correo", "contacto@parqueaventura.com.co")
            FilaContacto(Icons.Default.Schedule, "Horario", "Lun–Vie: 9am – 6pm\nSáb–Dom: 8am – 8pm")
            FilaContacto(Icons.Default.DirectionsBus,"Cómo llegar", "Ruta alimentadora desde Portal 80 (TransMilenio), cada 30 min")

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // ── DIÁLOGO MAPA AMPLIADO ──────────────────────────
    if (mostrarMapaCompleto) {
        Dialog(
            onDismissRequest = { mostrarMapaCompleto = false },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false  // quita el límite de ancho del sistema
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { mostrarMapaCompleto = false }
            ) {
                AsyncImage(
                    model = URL_MAPA,
                    contentDescription = "Mapa ampliado",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                // Botón cerrar
                IconButton(
                    onClick = { mostrarMapaCompleto = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
            }
        }
    }
}

// ── COMPONENTE FILA DE CONTACTO ────────────────────────
@Composable
fun FilaContacto(icono: ImageVector, etiqueta: String, valor: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = VerdeAccent,
                modifier = Modifier.size(22.dp).padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = etiqueta, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextoSecundario)
                Text(text = valor, fontSize = 14.sp, color = TextoPrincipal, lineHeight = 20.sp)
            }
        }
    }
}