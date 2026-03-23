package com.eam.parqueaventuraapp.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.R
import com.eam.parqueaventuraapp.ui.theme.AccentGreen
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun PantallaMisFavoritos(navController: NavController,viewModel: UsuarioViewModel) {
    // Scaffold proporciona la estructura básica con la barra de navegación inferior
    Scaffold(
        bottomBar = {
            // Pasamos el navController a la barra inferior
            BarraNavegacionFavoritos(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)) // Fondo gris claro
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Título de la sección
            Text(
                text = "Mis Favoritos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "1 atracciones guardadas",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de atracciones favoritas (usamos LazyColumn para eficiencia)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TarjetaAtraccionFavorita()
                }
            }
        }
    }
}

@Composable
fun TarjetaAtraccionFavorita() {
    // Card que contiene la información de la atracción
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de la atracción con etiqueta "Infantil"
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background), // Reemplazar con imagen real
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                // Etiqueta naranja "Infantil"
                Surface(
                    modifier = Modifier
                        .padding(2.dp)
                        .align(Alignment.TopStart),
                    color = Color(0xFFFF9800),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Infantil",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Información de la atracción
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 12.dp, horizontal = 4.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Carrusel Mágico",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Text(
                    text = "Un encantador carrusel para toda la familia con caballos coloridos y",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Etiqueta de tiempo
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "10 min",
                                color = Color(0xFF2E7D32),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    // Estado "Activa"
                    Text(
                        text = "Activa",
                        color = Color(0xFF2E7D32),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BarraNavegacionFavoritos(navController: NavController) {
    // Barra inferior personalizada para la pantalla de favoritos
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
            // Configuración para ir a la pantalla de inicio
            ItemNavegacion(
                icon = Icons.Default.Home,
                label = "Inicio",
                onClick = {/* Navegar a inicio cuando creemos la pantalla */}
            )

            // Configuración para atracciones
            ItemNavegacion(
                icon = Icons.Default.ConfirmationNumber,
                label = "Atracciones",
                onClick = {/* Navegar a Atracciones cuando creemos la pantalla */}
            )

            // "Favoritos" está seleccionado en esta pantalla, tiene fondo verde claro y color verde
            ItemNavegacion(
                icon = Icons.Default.Favorite,
                label = "Favoritos",
                isSelected = true
            )

            // Configuración para Mapa
            ItemNavegacion(
                icon = Icons.Default.Map,
                label = "Mapa",
                onClick = {navController.navigate("mapa_parque")}
            )

            // Configuración para Perfil
            ItemNavegacion(
                icon = Icons.Default.Person,
                label = "Perfil",
                onClick = { navController.navigate("perfil")}
            )
        }
    }
}

@Composable
fun ItemMenu(icon: ImageVector, label: String, isSelected: Boolean = false) {
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
