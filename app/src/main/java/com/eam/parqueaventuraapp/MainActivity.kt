package com.eam.parqueaventuraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eam.parqueaventuraapp.ui.theme.ParqueAventuraAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParqueAventuraAppTheme {
                // El Scaffold maneja automáticamente los espacios (paddings) de las barras del sistema
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Agregamos una Column con scroll o simplemente padding para contener los ejemplos
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        EjemploColumn()
                        EjemploRow()

                    }
                }
            }
        }
    }
}

@Composable
fun EjemploColumn() {
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.height(16.dp))
        Text("Crear cuenta")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Únete a la aventura hoy mismo")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) {
            Text("Crear Cuenta")
        }
    }
}

@Composable
fun EjemploRow(){
    Row (verticalAlignment = Alignment.CenterVertically){
        Text("Ya tienes cuenta? ")
        Text(" Inicia sesión")
    }
}
