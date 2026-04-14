package com.eam.parqueaventuraapp.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eam.parqueaventuraapp.ui.pantallas.PantallaLogin
import com.eam.parqueaventuraapp.ui.pantallas.PantallaRegistro
import com.eam.parqueaventuraapp.ui.pantallas.admins.AdminUsuariosPantalla
import com.eam.parqueaventuraapp.ui.pantallas.admins.PantallaCrearAtraccion
import com.eam.parqueaventuraapp.ui.pantallas.admins.PantallaEditarAtraccion
import com.eam.parqueaventuraapp.ui.pantallas.admins.PantallaGestionAtracciones
import com.eam.parqueaventuraapp.ui.pantallas.admins.PantallaInicioAdmin
import com.eam.parqueaventuraapp.ui.pantallas.usuarios.PantallaInicio
import com.eam.parqueaventuraapp.ui.pantallas.usuarios.PantallaMapaUsuario
import com.eam.parqueaventuraapp.ui.pantallas.usuarios.PantallaMiPerfil
import com.eam.parqueaventuraapp.ui.pantallas.usuarios.PantallaMisFavoritos
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun AppNavigation(
    userViewModel: UsuarioViewModel,
    atraccionViewModel: AtraccionViewModel
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        // Definición de las rutas de navegación
        composable("login") { PantallaLogin(navController, userViewModel) }
        composable("registro") { PantallaRegistro(navController, userViewModel) }
        composable("perfil") { PantallaMiPerfil(navController, userViewModel) }
        composable("admin_usuarios") { AdminUsuariosPantalla(navController, userViewModel) }
        composable("mapa_parque"){ PantallaMapaUsuario(navController) }
        composable("favoritos"){ PantallaMisFavoritos(navController, userViewModel) }
        composable("inicioUsuario"){ PantallaInicio(navController, userViewModel, atraccionViewModel) }
        composable("panelAdmin"){ PantallaInicioAdmin(navController, userViewModel, atraccionViewModel) }
        composable("gestionAtracciones"){ PantallaGestionAtracciones(navController, atraccionViewModel) }
        composable("crearAtraccion"){ PantallaCrearAtraccion(navController, atraccionViewModel) }

        composable("editarAtraccion/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
            PantallaEditarAtraccion(navController, atraccionViewModel, atraccionId = id)
        }
    }
}
