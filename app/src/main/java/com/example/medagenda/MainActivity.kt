import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.medagenda.navigation.AppNavGraph
import com.example.medagenda.ui.theme.MedAgendaTheme

// Es recomendable renombrar el paquete a com.medagenda.app
// Puedes hacerlo con Refactor > Rename en Android Studio.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Aquí establecemos el contenido de la actividad.
        // Llamamos a la función raíz que contendrá toda nuestra UI.
        setContent {
            MedAgendaApp() // Llamada a la función principal de la app
        }
    }
}

/**
 * Composable raíz que inicializa el entorno de la aplicación MedAgenda.
 *
 * Configura el tema de la aplicación (MedAgendaTheme), el controlador de navegación
 * y la superficie principal sobre la que se dibujará toda la interfaz.
 */
@Composable
fun MedAgendaApp() {
    // 1. Configura el tema personalizado de la app (colores, tipografía)
    MedAgendaTheme {
        // 2. Crea y recuerda el controlador que gestionará la navegación entre pantallas.
        val navController = rememberNavController()
        // 3. Surface actúa como el lienzo de fondo principal de la aplicación.
        Surface(color = MaterialTheme.colorScheme.background) {
            // 4. AppNavGraph contiene la lógica de navegación (el NavHost) y la
            //    estructura principal de la UI (Scaffold, TopAppBar, etc.).
            AppNavGraph(navController = navController)
        }
    }
}