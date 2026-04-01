package com.eam.parqueaventuraapp.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BarraNavegacionInferior(navController: NavController) {

    val rutaActual = navController.currentBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ItemNavegacion(
                icon = Icons.Default.Home,
                label = "Inicio",
                isSelected = rutaActual == "inicioUsuario",
                onClick = { navController.navigate("inicioUsuario") }
            )

            ItemNavegacion(
                icon = Icons.Default.ConfirmationNumber,
                label = "Atracciones",
                isSelected = rutaActual == "atracciones",
                onClick = { navController.navigate("atracciones") }
            )

            ItemNavegacion(
                icon = Icons.Default.FavoriteBorder,
                label = "Favoritos",
                isSelected = rutaActual == "favoritos",
                onClick = { navController.navigate("favoritos") }
            )

            ItemNavegacion(
                icon = Icons.Default.Map,
                label = "Mapa",
                isSelected = rutaActual == "mapa_parque",
                onClick = { navController.navigate("mapa_parque") }
            )

            ItemNavegacion(
                icon = Icons.Default.Person,
                label = "Perfil",
                isSelected = rutaActual == "perfil",
                onClick = { navController.navigate("perfil") }
            )
        }
    }
}