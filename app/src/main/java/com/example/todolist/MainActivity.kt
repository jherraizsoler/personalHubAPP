package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todolist.authentication.view.AuthScreen
import com.example.todolist.authentication.viewmodel.AuthViewModel
import com.example.todolist.seccion.estudio.ui.EstudioScreen
import com.example.todolist.seccion.finanzas.ui.FinanzasScreen
import com.example.todolist.seccion.productividad.ProductivityNavHost
import com.example.todolist.seccion.salud.ui.SaludScreen
import com.example.todolist.ui.theme.ToDoListTheme
import androidx.fragment.app.FragmentActivity
import android.os.Build
import androidx.annotation.RequiresApi

// Definir las rutas de nivel superior
sealed class AppScreen(val route: String, val title: String, val icon: ImageVector) {
    object Productivity : AppScreen("productivity", "Productividad", Icons.Default.CheckCircle)
    object Finanzas : AppScreen("finanzas", "Finanzas", Icons.Default.MonetizationOn)
    object Salud : AppScreen("salud", "Salud", Icons.Default.SelfImprovement)
    object Estudio : AppScreen("estudio", "Estudio", Icons.Default.List)
}

// *** ¡La clave está aquí! Cambiamos de ComponentActivity a FragmentActivity. ***
class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Usamos by viewModels() para obtener la instancia del ViewModel de forma más idiomática
        val authViewModel: AuthViewModel by viewModels { AuthViewModel.Companion.Factory(this.application) }

        // *** ¡El cambio clave está aquí! ***
        // Llamamos a este método para forzar que el estado de login sea 'false'
        // cada vez que la actividad se crea. Esto hace que la pantalla de autenticación
        // se muestre y se active el prompt biométrico.
        authViewModel.logout()

        setContent {
            ToDoListTheme {
                // Recolecta el estado de login del ViewModel
                val uiState by authViewModel.uiState.collectAsState()

                // Añadido para intentar la autenticación biométrica al inicio de la app.
                // Usamos LaunchedEffect para que esta lógica se ejecute solo una vez
                // al iniciar la composición de la MainActivity y el estado no esté logueado.
                LaunchedEffect(key1 = Unit) {
                    if (!uiState.isLoggedIn && authViewModel.hasBiometricCapability()) {
                        authViewModel.authenticateWithBiometrics(
                            // `this@MainActivity` es el FragmentActivity requerido.
                            activity = this@MainActivity,
                            onAuthSuccess = {
                                // La lógica de tu ViewModel ya se encarga de actualizar el estado de login.
                            }
                        )
                    }
                }

                // Usa la lógica condicional para decidir qué pantalla mostrar
                if (uiState.isLoggedIn) {
                    // Si el usuario está logueado, muestra la pantalla principal de la app
                    AppNavigation()
                } else {
                    // Si no está logueado, muestra la pantalla de autenticación
                    AuthScreen(
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Lógica para determinar la pantalla actual basada en la ruta del NavHost anidado
    val currentScreen = when {
        // Usa `startsWith` para manejar subrutas, como las de "ProductivityNavHost"
        currentRoute?.startsWith(AppScreen.Productivity.route) == true -> AppScreen.Productivity
        currentRoute?.startsWith(AppScreen.Finanzas.route) == true -> AppScreen.Finanzas
        currentRoute?.startsWith(AppScreen.Salud.route) == true -> AppScreen.Salud
        currentRoute?.startsWith(AppScreen.Estudio.route) == true -> AppScreen.Estudio
        else -> AppScreen.Productivity
    }

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Productivity.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Productivity.route) {
                // Ahora la llamada es correcta, sin el parámetro appRepository.
                ProductivityNavHost(
                    parentNavController = navController
                )
            }
            composable(AppScreen.Finanzas.route) {
                FinanzasScreen(navController = navController)
            }
            composable(AppScreen.Salud.route) {
                SaludScreen(navController = navController)
            }
            composable(AppScreen.Estudio.route) {
                EstudioScreen(navController = navController)
            }
        }
    }
}
