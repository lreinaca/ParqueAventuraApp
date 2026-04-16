package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
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
    atraccionId: Int
) {
    val atracciones by atraccionViewModel.atracciones.collectAsState()
    val atraccion = atracciones.firstOrNull { it.id == atraccionId }

    var favorito by remember { mutableStateOf(false) }

    if (atraccion == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = VerdeAccent)
        }
        return
    }

    val (nivelIntensidad, valorIntensidad, colorIntensidad) = when (atraccion.tipo.lowercase()) {
        "infantil" -> Triple("Muy Baja", 0.2f, ColorInfantil)
        "familiar" -> Triple("Media", 0.5f, ColorFamiliar)
        "extrema" -> Triple("Alta", 0.85f, ColorExtrema)
        else -> Triple("Normal", 0.5f, Color.Gray)
    }

    val zonaParque = when (atraccion.tipo.lowercase()) {
        "infantil" -> "Zona Infantil"
        "familiar" -> "Zona Familiar"
        "extrema" -> "Zona Extrema"
        else -> "Zona General"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            AsyncImage(
                model = atraccion.imagen,
                contentDescription = atraccion.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.85f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextoPrincipal
                    )
                }

                IconButton(
                    onClick = { favorito = !favorito },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.85f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (favorito) Color.Red else TextoPrincipal
                    )
                }
            }

            BadgeCategoria(
                tipo = atraccion.tipo,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = atraccion.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = TextoPrincipal,
                    modifier = Modifier.weight(1f)
                )
                BadgeEstado(estado = atraccion.estado)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TarjetaEstadistica(
                    icono = Icons.Default.Timer,
                    valor = atraccion.tiempoEspera.toString(),
                    etiqueta = "min espera",
                    colorIcono = ColorInfantil
                )
                TarjetaEstadistica(
                    icono = Icons.Default.FlashOn,
                    valor = when (atraccion.tipo.lowercase()) {
                        "infantil" -> "1"
                        "familiar" -> "3"
                        "extrema"  -> "5"
                        else       -> "2"
                    },
                    etiqueta = "intensidad",
                    colorIcono = ColorInfantil
                )
                TarjetaEstadistica(
                    icono = Icons.Default.Star,
                    valor = "4.8",
                    etiqueta = "rating",
                    colorIcono = Color(0xFFFFC107)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FondoPantalla),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nivel de Intensidad",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextoPrincipal
                        )
                        Text(
                            text = nivelIntensidad,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = colorIntensidad
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { valorIntensidad },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = colorIntensidad,
                        trackColor = Color.LightGray.copy(alpha = 0.3f),
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FondoPantalla),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Descripción",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextoPrincipal
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Atracción de tipo ${atraccion.tipo} con una duración de ${atraccion.duracion} minutos. " +
                                "Tiempo de espera estimado: ${atraccion.tiempoEspera} minutos. " +
                                "Estado actual: ${atraccion.estado.lowercase().replaceFirstChar { it.uppercase() }}.",
                        fontSize = 14.sp,
                        color = TextoSecundario,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FondoPantalla),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(VerdeAccent.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Ubicación",
                            tint = VerdeAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Ubicación en el parque",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextoPrincipal
                        )
                        Text(
                            text = zonaParque,
                            fontSize = 13.sp,
                            color = TextoSecundario
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { favorito = !favorito },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeAccent)
            ) {
                Icon(
                    imageVector = if (favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (favorito) "Guardado en favoritos" else "Agregar a favoritos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TarjetaEstadistica(
    icono: ImageVector,
    valor: String,
    etiqueta: String,
    colorIcono: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = etiqueta,
            tint = colorIcono,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = valor,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = TextoPrincipal
        )
        Text(
            text = etiqueta,
            fontSize = 12.sp,
            color = TextoSecundario
        )
    }
}
