package com.example.todolist.seccion.productividad

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.seccion.productividad.ui.MainScreen
import com.example.todolist.seccion.productividad.ui.composables.HistoryScreens
import com.example.todolist.seccion.productividad.viewmodel.ProductivityViewModel
import com.example.todolist.seccion.productividad.viewmodel.ProductivityViewModelFactory

sealed class ProductivityScreen(val route: String) {
    object Main : ProductivityScreen("productivity_main")
    object History : ProductivityScreen("history_screen/{itemType}") {
        fun createRoute(itemType: String) = "history_screen/$itemType"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
// Se ha eliminado el parámetro 'appRepository' de la firma de la función.
fun ProductivityNavHost(parentNavController: NavController) {
    val productivityNavController = rememberNavController()
    val context = LocalContext.current

    val todoViewModel: ProductivityViewModel = viewModel(
        factory = ProductivityViewModelFactory(context)
    )

    NavHost(
        navController = productivityNavController,
        startDestination = ProductivityScreen.Main.route
    ) {
        composable(ProductivityScreen.Main.route) {
            MainScreen(
                navController = productivityNavController,
                todoViewModel = todoViewModel,
                parentNavController = parentNavController
            )
        }

        composable(
            route = ProductivityScreen.History.route,
            arguments = listOf(navArgument("itemType") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemType = backStackEntry.arguments?.getString("itemType") ?: "tasks"
            val isTasks = itemType == "tasks"
            HistoryScreens(
                navController = productivityNavController,
                isTasks = isTasks,
                productivityViewModel = todoViewModel
            )
        }
    }
}
