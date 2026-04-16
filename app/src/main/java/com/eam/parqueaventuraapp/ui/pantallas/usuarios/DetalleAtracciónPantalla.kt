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
import androidx.compose.material.icons.filled.Share
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

// PANTALLA DE DETALLE DE UNA ATRACCIÓN

@Composable
fun PantallaDetalleAtraccion(
    navController: NavController,
    atraccionViewModel: AtraccionViewModel,
    atraccionId: Int
) {
    // obtenemos la lista del viewModel y buscamos por id

    val atracciones by atraccionViewModel.atracciones.collectAsState()
    val atraccion = atracciones.firstOrNull { it.id == atraccionId }


    var favorito by remember { mutableStateOf(false) }

    // si todavia no carga la atraccion mostramos un spinner
    if (atraccion == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = VerdeAccent)
        }
        return
    }

    // calculamos la intensidad según el tipo de atracción

    val (nivelIntensidad, valorIntensidad, colorIntensidad) = when (atraccion.tipo.lowercase()) {
        "infantil" -> Triple("Muy Baja", 0.2f, ColorInfantil)
        "familiar" -> Triple("Media", 0.5f, ColorFamiliar)
        "extrema"  -> Triple("Alta", 0.85f, ColorExtrema)
        else       -> Triple("Normal", 0.5f, Color.Gray)
    }

    // zona para saber en que parte del parque queda
    val zonaParque = when (atraccion.tipo.lowercase()) {
        "infantil" -> "Zona Infantil"
        "familiar" -> "Zona Familiar"
        "extrema"  -> "Zona Extrema"
        else       -> "Zona General"
    }

    // contenido principal con scroll vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .verticalScroll(rememberScrollState())
    ) {


        // IMAGEN GRANDE CON BOTONES SUPERPUESTOS

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            // imagen de fondo que ocupa todo el recuadro
            AsyncImage(
                model = atraccion.imagen,
                contentDescription = atraccion.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // fila superior: botón volver + compartir + favorito
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // boton redondo para volver atrás
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

                // agrupamos compartir y favorito a la derecha
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { /* compartir - pendiente */ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.85f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
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
            }

            // etiquetita de la categoria abajo a la izquierda de la imagen

            BadgeCategoria(
                tipo = atraccion.tipo,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }


        // CONTENIDO BLANCO INFERIOR

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(20.dp)
        ) {

            // --- Nombre de la atraccion + badge de estado ---
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

                // reutilizamos el badge de estado que ya tenemos
                BadgeEstado(estado = atraccion.estado)
            }

            Spacer(modifier = Modifier.height(20.dp))


            // TARJETAS DE ESTADÍSTICAS (tiempo, intensidad, rating)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // tarjeta de minutos de espera
                TarjetaEstadistica(
                    icono = Icons.Default.Timer,
                    valor = atraccion.tiempoEspera.toString(),
                    etiqueta = "min espera",
                    colorIcono = ColorInfantil
                )

                // tarjeta de intensidad (numero)
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

                // tarjeta de rating (valor fijo por ahora)
                TarjetaEstadistica(
                    icono = Icons.Default.Star,
                    valor = "4.8",
                    etiqueta = "rating",
                    colorIcono = Color(0xFFFFC107)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            // BARRA DE NIVEL DE INTENSIDAD

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FondoPantalla),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // titulo y texto del nivel a los lados
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

                    // barra de progreso que muestra visualmente el nivel
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


            // SECCIÓN DE DESCRIPCIÓN

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


            // UBICACIÓN EN EL PARQUE

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
                    // circulito verde con el icono de ubicación
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


            // BOTÓN DE AGREGAR A FAVORITOS

            Button(
                onClick = { favorito = !favorito },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeAccent
                )
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

            // espacio extra al final para que no quede pegado abajo
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



// TARJETA DE ESTADÍSTICA (icono + número + etiqueta)

@Composable
fun TarjetaEstadistica(
    icono: ImageVector,
    valor: String,
    etiqueta: String,
    colorIcono: Color
) {
    // cada tarjetica muestra un dato concreto de la atraccion
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

// BADGE DE CATEGORÍA (Infantil / Familiar / Extrema)

@Composable
fun BadgeCategoria(
    tipo: String,
    modifier: Modifier = Modifier
) {
    val colorFondo = when (tipo.lowercase()) {
        "infantil" -> ColorInfantil
        "familiar" -> ColorFamiliar
        "extrema" -> ColorExtrema
        else -> Color.Gray
    }

    Box(
        modifier = modifier
            .background(colorFondo, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = tipo.replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// BADGE DE ESTADO (Abierta / Cerrada / Mantenimiento)

@Composable
fun BadgeEstado(estado: String) {
    val (texto, fondo, colorTexto) = when (estado.uppercase()) {
        "ABIERTA" -> Triple("Abierta", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "CERRADA" -> Triple("Cerrada", Color(0xFFFFEBEE), Color(0xFFC62828))
        "MANTENIMIENTO" -> Triple("Mantenimiento", Color(0xFFFFF8E1), Color(0xFFF57F17))
        else -> Triple(estado, Color(0xFFF5F5F5), TextoSecundario)
    }

    Box(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = texto,
            color = colorTexto,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
