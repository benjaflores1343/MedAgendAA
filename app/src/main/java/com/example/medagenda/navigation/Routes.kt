package com.example.medagenda.navigation

// Clase sellada para rutas: evita "strings mágicos" y facilita refactors
sealed class Route(val definition: String) { // Cada objeto representa una pantalla
    data object Login    : Route("login")    // Ruta Login
    data object Register : Route("register") // Ruta Registro

    // Home es especial porque necesita argumentos.
    // `definition` contiene el patrón para el NavGraph.
    // La función `build` crea la ruta específica para navegar.
    data object Home : Route("home/{userName}/{userRole}") {
        fun build(userName: String, userRole: String): String {
            return definition
                .replace("{userName}", userName)
                .replace("{userRole}", userRole)
        }
    }
}

/*
* “Strings mágicos” se refiere a cuando pones un texto duro y repetido en varias partes del código,
* Si mañana cambias "home" por "inicio", tendrías que buscar todas las ocurrencias de "home" a mano.
* Eso es frágil y propenso a errores.
La idea es: mejor centralizar esos strings en una sola clase (Route), y usarlos desde ahí.*/
