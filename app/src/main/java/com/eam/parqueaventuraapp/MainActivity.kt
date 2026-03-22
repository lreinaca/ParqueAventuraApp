package com.eam.parqueaventuraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eam.parqueaventuraapp.data.model.database.BaseDeDatosApp
import com.eam.parqueaventuraapp.data.model.repository.UsuarioRepositorio
import com.eam.parqueaventuraapp.ui.theme.ParqueAventuraAppTheme
import com.eam.parqueaventuraapp.ui.theme.pantallas.AdminUsuariosPantalla
import com.eam.parqueaventuraapp.ui.theme.pantallas.PantallaRegistro
import com.eam.parqueaventuraapp.ui.theme.viewModel.UsuarioViewModel
import com.eam.parqueaventuraapp.ui.theme.viewModel.UsuarioViewModelFactory

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
                        composable("login") { PantallaLogin(navController) }
                        composable("registro") { PantallaRegistro(navController, userViewModel) }
                        // Ruta para la nueva pantalla de administración
                        composable("admin_usuarios") { AdminUsuariosPantalla(navController, userViewModel) }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaLogin(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido al Parque")
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { navController.navigate("registro") }, modifier = Modifier.fillMaxWidth(0.7f)) {
            Text("Ir a Registro")
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        // Botón para navegar a la pantalla de administración
        OutlinedButton(onClick = { navController.navigate("admin_usuarios") }, modifier = Modifier.fillMaxWidth(0.7f)) {
            Text("Administrar Usuarios")
        }
    }
}
