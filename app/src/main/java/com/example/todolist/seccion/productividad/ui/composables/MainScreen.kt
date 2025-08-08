package com.example.todolist.seccion.productividad.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.todolist.seccion.productividad.ProductivityScreen
import com.example.todolist.seccion.productividad.data.TaskType
import com.example.todolist.seccion.productividad.ui.composables.AddDialog
import com.example.todolist.seccion.productividad.ui.composables.TaskItem
import com.example.todolist.seccion.productividad.viewmodel.ProductivityViewModel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import com.example.todolist.seccion.productividad.data.Task
import com.example.todolist.seccion.productividad.ui.composables.CategoriesDialog
import com.example.todolist.seccion.productividad.ui.composables.EditDialog
import com.example.todolist.ui.theme.CustomTopAppBar

@Composable
fun CategoryChip(
    categoria: String,
    onDismiss: () -> Unit
) {
    AssistChip(
        onClick = {},
        label = { Text(categoria) },
        modifier = Modifier.padding(horizontal = 4.dp),
        trailingIcon = {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Desmarcar categoría"
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    todoViewModel: ProductivityViewModel,
    parentNavController: NavController
) {
    val allCategories by todoViewModel.allCategories.collectAsState()
    val allItems by todoViewModel.items.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Tareas", "Ideas")

    var showAddDialog by remember { mutableStateOf(false) }

    var showEditDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    // --- NUEVO: Estado para el diálogo de categorías ---
    var showCategoriesDialog by remember { mutableStateOf(false) }
    var categoriesToShow by remember { mutableStateOf(emptyList<String>()) }
    // ----------------------------------------------------

    var expanded by remember { mutableStateOf(false) }
    var selectedCategories by remember { mutableStateOf(emptyList<String>()) }

    val filteredItems = if (selectedCategories.isEmpty()) {
        allItems.filter { it.type == (if (selectedTabIndex == 0) TaskType.TASK else TaskType.IDEA) && !it.isDeleted }
    } else {
        allItems.filter { item ->
            item.type == (if (selectedTabIndex == 0) TaskType.TASK else TaskType.IDEA) && !item.isDeleted && item.categories.any { it in selectedCategories }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { paddingValues ->
        if (showAddDialog) {
            val taskType = if (selectedTabIndex == 0) TaskType.TASK else TaskType.IDEA
            AddDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { name, categories, content -> // <-- AÑADIDO: Recibe el parámetro 'content'
                    todoViewModel.addItem(name, categories, taskType, content) // <-- AÑADIDO: Pasa el 'content'
                    showAddDialog = false
                },
                dialogTitle = if (taskType == TaskType.TASK) "Añadir Tarea" else "Añadir Idea",
                labelName = "Nombre",
                taskType = taskType, // <-- AÑADIDO: Pasamos el tipo de tarea
                todoViewModel = todoViewModel
            )
        }

        if (showEditDialog && taskToEdit != null) {
            EditDialog(
                onDismiss = { showEditDialog = false },
                onEdit = { updatedTask ->
                    todoViewModel.updateItem(updatedTask)
                    showEditDialog = false
                },
                dialogTitle = "Editar ${taskToEdit!!.name}",
                taskToEdit = taskToEdit!!,
                todoViewModel = todoViewModel
            )
        }

        // --- NUEVO: Diálogo para mostrar las categorías ---
        if (showCategoriesDialog) {
            CategoriesDialog(
                categories = categoriesToShow,
                onDismiss = { showCategoriesDialog = false }
            )
        }
        // ---------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 1.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    onClick = {
                        val type = if (selectedTabIndex == 0) "tasks" else "ideas"
                        navController.navigate(ProductivityScreen.History.createRoute(type))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Historial"
                    )
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(selectedCategories) { categoria ->
                    CategoryChip(
                        categoria = categoria,
                        onDismiss = { selectedCategories = selectedCategories - categoria }
                    )
                }
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                TextField(
                    value = if (selectedCategories.isEmpty()) "Filtrar por categoría" else "${selectedCategories.size} categorías seleccionadas",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    allCategories.forEach { category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = category in selectedCategories,
                                onCheckedChange = { isChecked ->
                                    selectedCategories = if (isChecked) {
                                        selectedCategories + category
                                    } else {
                                        selectedCategories - category
                                    }
                                }
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(text = category, modifier = Modifier.weight(1f))

                            IconButton(
                                onClick = {
                                    todoViewModel.deleteCategory(category)
                                    expanded = false
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar categoría",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filteredItems) { item ->
                    TaskItem(
                        task = item,
                        onToggleTask = { todoViewModel.toggleItemCompletion(it) },
                        onArchiveTask = { todoViewModel.softDeleteItem(it) },
                        onEditClick = { task ->
                            taskToEdit = task
                            showEditDialog = true
                        },
                        onShowCategoriesClick = { categories -> // <-- Lógica para el nuevo diálogo
                            categoriesToShow = categories
                            showCategoriesDialog = true
                        }
                    )
                }
            }
        }
    }
}