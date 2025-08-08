package com.example.todolist.seccion.salud.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // <-- Importa LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.seccion.salud.data.Pastilla
import com.example.todolist.seccion.salud.data.RegistroAlimentacion
import com.example.todolist.seccion.salud.viewmodel.SaludViewModel
import com.example.todolist.seccion.salud.viewmodel.SaludViewModelFactory
import com.example.todolist.ui.theme.CustomTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaludScreen(
    navController: NavController,
    modifier: Modifier = Modifier
    // El repositorio ya no se recibe como parámetro
) {
    // Obtenemos el contexto y lo usamos para crear la instancia del ViewModel
    val context = LocalContext.current
    val saludViewModel: SaludViewModel = viewModel(
        factory = SaludViewModelFactory(context)
    )

    var showAddPastillaDialog by remember { mutableStateOf(false) }
    var showAddComidaDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { CustomTopAppBar(navController = navController, title = "Salud") },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(onClick = { showAddPastillaDialog = true }, modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("Pill")
                }
                FloatingActionButton(onClick = { showAddComidaDialog = true }) {
                    Text("Food")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "Pastillas", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
            LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(saludViewModel.pastillas) { pastilla ->
                    PastillaCard(pastilla = pastilla, onEliminarClick = { saludViewModel.eliminarPastilla(pastilla) })
                }
            }
            Text(text = "Registro de Comidas", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
            LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(saludViewModel.registrosAlimentacion) { registro ->
                    RegistroComidaItem(registro = registro, onEliminarClick = { saludViewModel.eliminarRegistroAlimentacion(registro) })
                }
            }
        }
    }

    if (showAddPastillaDialog) {
        AddPastillaDialog(onDismiss = { showAddPastillaDialog = false }, onConfirm = { nombre, dosis, frecuencia ->
            saludViewModel.añadirPastilla(nombre, dosis, frecuencia)
            showAddPastillaDialog = false
        })
    }

    if (showAddComidaDialog) {
        AddComidaDialog(onDismiss = { showAddComidaDialog = false }, onConfirm = { comida, calorias, notas ->
            saludViewModel.añadirRegistroAlimentacion(comida, System.currentTimeMillis(), calorias, notas)
            showAddComidaDialog = false
        })
    }
}