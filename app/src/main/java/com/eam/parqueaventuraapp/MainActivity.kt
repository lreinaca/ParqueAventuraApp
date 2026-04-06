package com.eam.parqueaventuraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eam.parqueaventuraapp.data.modelo.database.BaseDeDatosApp
import com.eam.parqueaventuraapp.data.modelo.repository.UsuarioRepositorio
import com.eam.parqueaventuraapp.data.repository.AtraccionRepositorio
import com.eam.parqueaventuraapp.ui.pantallas.admins.AdminUsuariosPantalla
import com.eam.parqueaventuraapp.ui.pantallas.PantallaLogin
import com.eam.parqueaventuraapp.ui.pantallas.usuarios.PantallaMapaUsuario
import com.eam.parqueaventuraapp.ui.pantallas.usuarios.PantallaMiPerfil
import com.eam.parqueaventuraapp.ui.pantallas.usuarios.PantallaMisFavoritos
import com.eam.parqueaventuraapp.ui.pantallas.PantallaRegistro
import com.eam.parqueaventuraapp.ui.pantallas.admins.PantallaCrearAtraccion
import com.eam.parqueaventuraapp.ui.pantallas.admins.PantallaGestionAtracciones
import com.eam.parqueaventuraapp.ui.pantallas.admins.PantallaInicioAdmin
import com.eam.parqueaventuraapp.ui.theme.ParqueAventuraAppTheme
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModelFactory
import com.eam.parqueaventuraapp.ui.pantallas.usuarios.PantallaInicio
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = BaseDeDatosApp.obtenerBaseDeDatos(this)

        //usuario
        val userRepositorio = UsuarioRepositorio(database.usuarioDao())
        val factory = UsuarioViewModelFactory(userRepositorio)

        //atracción
        val atraccionepositorio = AtraccionRepositorio(database.atraccionDao())
        val atraccionfactory = AtraccionViewModelFactory(atraccionepositorio)


        enableEdgeToEdge()
        setContent {
            ParqueAventuraAppTheme {
                val navController = rememberNavController()
                val userViewModel: UsuarioViewModel = viewModel(factory = factory)
                val atraccionViewModel: AtraccionViewModel = viewModel(factory = atraccionfactory)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Definición de las rutas de navegación
                        composable("login") { PantallaLogin(navController, userViewModel) }
                        composable("registro") { PantallaRegistro(navController, userViewModel) }
                        composable("perfil") { PantallaMiPerfil(navController, userViewModel) }
                        composable("admin_usuarios") { AdminUsuariosPantalla(navController, userViewModel) }
                        composable ("mapa_parque"){ PantallaMapaUsuario(navController) }
                        composable ("favoritos"){ PantallaMisFavoritos(navController,userViewModel) }
                        composable("inicioUsuario"){PantallaInicio(navController, userViewModel, atraccionViewModel)}
                        composable("panelAdmin"){ PantallaInicioAdmin(navController, userViewModel, atraccionViewModel) }
                        composable ("gestionAtracciones"){ PantallaGestionAtracciones(navController, atraccionViewModel) }
                        composable("crearAtraccion"){ PantallaCrearAtraccion(navController, atraccionViewModel) }
                    }
                }
            }
        }
    }
}
