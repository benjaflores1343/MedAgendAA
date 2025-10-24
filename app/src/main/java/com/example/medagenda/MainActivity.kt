package com.example.medagenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.medagenda.navigation.NavGraph
import com.example.medagenda.ui.theme.MedAgendaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Usamos el tema que ya creaste
            MedAgendaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Creamos el controlador de navegación
                    val navController = rememberNavController()
                    // Y se lo pasamos a nuestro grafo de navegación
                    NavGraph(navController = navController)
                }
            }
        }
    }
}