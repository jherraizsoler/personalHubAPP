package com.example.todolist

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todolist.seccion.estudio.ui.EstudioScreen
import com.example.todolist.seccion.finanzas.ui.FinanzasScreen
import com.example.todolist.seccion.productividad.ProductivityNavHost
import com.example.todolist.seccion.productividad.ProductivityScreen
import com.example.todolist.seccion.productividad.data.repository.TodoRepository
import com.example.todolist.seccion.salud.ui.SaludScreen
import com.example.todolist.ui.theme.ToDoListTheme

// Definir las rutas de nivel superior
// Define tus pantallas principales de la aplicación
sealed class AppScreen(val route: String, val title: String, val icon: ImageVector) {
    object Productivity : AppScreen("productivity", "Productividad", Icons.Default.CheckCircle)
    object Finanzas : AppScreen("finanzas", "Finanzas", Icons.Default.MonetizationOn)
    object Salud : AppScreen("salud", "Salud", Icons.Default.SelfImprovement)
    object Estudio : AppScreen("estudio", "Estudio", Icons.Default.List)
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                AppNavigation()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val appRepository = remember { TodoRepository(context) }

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
                onNavigate = { screen -> navController.navigate(screen.route) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Productivity.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Productivity.route) {
                ProductivityNavHost(
                    parentNavController = navController,
                    appRepository = appRepository
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