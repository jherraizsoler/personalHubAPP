package com.example.todolist.seccion.productividad.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.seccion.productividad.data.TaskType
import com.example.todolist.seccion.productividad.viewmodel.ProductivityViewModel
import com.example.todolist.ui.theme.CustomTopAppBar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreens(
    navController: NavController,
    isTasks: Boolean,
    productivityViewModel: ProductivityViewModel
) {
    val items by productivityViewModel.items.collectAsState()

    val filteredItems = items.filter {
        if (isTasks) it.type == TaskType.TASK else it.type == TaskType.IDEA
    }

    val completedItems = filteredItems.filter { it.isCompleted && !it.isDeleted }
    val uncompletedItems = filteredItems.filter { !it.isCompleted && !it.isDeleted }
    val deletedItems = filteredItems.filter { it.isDeleted }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                title = if (isTasks) "Historial de Tareas" else "Historial de Ideas"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Sección de Tareas/Ideas completadas
            HistorySection(
                title = "Completadas (${completedItems.size})",
                items = completedItems,
                onRestore = { productivityViewModel.restoreItem(it) },
                onPermanentlyDelete = { productivityViewModel.permanentlyDeleteItem(it) }
            )

            // Sección de Tareas/Ideas creadas (no completadas ni eliminadas)
            HistorySection(
                title = "Creadas (${uncompletedItems.size})",
                items = uncompletedItems,
                onRestore = { productivityViewModel.restoreItem(it) }, // Lógica de restauración si aplica
                onPermanentlyDelete = { productivityViewModel.permanentlyDeleteItem(it) }
            )

            // Sección de Tareas/Ideas eliminadas
            HistorySection(
                title = "Eliminadas (${deletedItems.size})",
                items = deletedItems,
                onRestore = { productivityViewModel.restoreItem(it) },
                onPermanentlyDelete = { productivityViewModel.permanentlyDeleteItem(it) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistorySection(
    title: String,
    items: List<com.example.todolist.seccion.productividad.data.Task>,
    onRestore: (com.example.todolist.seccion.productividad.data.Task) -> Unit,
    onPermanentlyDelete: (com.example.todolist.seccion.productividad.data.Task) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(16.dp)
    )
    if (items.isEmpty()) {
        Text("No hay elementos en esta sección.", modifier = Modifier.padding(horizontal = 16.dp))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
        ) {
            items(items) { item ->
                HistoryItemCard(
                    item = item,
                    onRestore = { onRestore(item) },
                    onPermanentlyDelete = { onPermanentlyDelete(item) }
                )
            }
        }
    }
}