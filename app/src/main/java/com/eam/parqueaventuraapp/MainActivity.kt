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
import com.eam.parqueaventuraapp.ui.pantallas.AdminUsuariosPantalla
import com.eam.parqueaventuraapp.ui.pantallas.PantallaLogin
import com.eam.parqueaventuraapp.ui.pantallas.PantallaMapaUsuario
import com.eam.parqueaventuraapp.ui.pantallas.PantallaMiPerfil
import com.eam.parqueaventuraapp.ui.pantallas.PantallaMisFavoritos
import com.eam.parqueaventuraapp.ui.pantallas.PantallaRegistro
import com.eam.parqueaventuraapp.ui.theme.ParqueAventuraAppTheme
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = BaseDeDatosApp.obtenerBaseDeDatos(this)
        val repositorio = UsuarioRepositorio(database.usuarioDao())
        val factory = UsuarioViewModelFactory(repositorio)

        enableEdgeToEdge()
        setContent {
            ParqueAventuraAppTheme {
                val navController = rememberNavController()
                val userViewModel: UsuarioViewModel = viewModel(factory = factory)

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
                        composable ("favoritos"){ PantallaMisFavoritos(navController) }
                    }
                }
            }
        }
    }
}
