package com.eam.parqueaventuraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eam.parqueaventuraapp.data.modelo.database.BaseDeDatosApp
import com.eam.parqueaventuraapp.data.modelo.repository.UsuarioRepositorio
import com.eam.parqueaventuraapp.data.network.RetrofitClient
import com.eam.parqueaventuraapp.data.repository.AtraccionRepositorio
import com.eam.parqueaventuraapp.ui.navegacion.AppNavigation
import com.eam.parqueaventuraapp.ui.theme.ParqueAventuraAppTheme
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModelFactory
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Configuramos la base de datos y repositorios una sola vez
        val database = BaseDeDatosApp.obtenerBaseDeDatos(this)
        val userRepositorio = UsuarioRepositorio(database.usuarioDao())
        val userFactory = UsuarioViewModelFactory(userRepositorio)

        // AHORA: Usamos el servicio de Retrofit en lugar del DAO
        val atraccionRepositorio = AtraccionRepositorio(RetrofitClient.apiService)
        val atraccionFactory = AtraccionViewModelFactory(atraccionRepositorio)

        enableEdgeToEdge()
        
        setContent {
            ParqueAventuraAppTheme {
                val userViewModel: UsuarioViewModel = viewModel(factory = userFactory)
                val atraccionViewModel: AtraccionViewModel = viewModel(factory = atraccionFactory)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(
                            userViewModel = userViewModel,
                            atraccionViewModel = atraccionViewModel
                        )
                    }
                }
            }
        }
    }
}
