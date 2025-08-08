package com.example.todolist.seccion.productividad.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.seccion.productividad.data.Task
import com.example.todolist.seccion.productividad.data.TaskType
import com.example.todolist.seccion.productividad.viewmodel.ProductivityViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    onDismiss: () -> Unit,
    onEdit: (Task) -> Unit,
    dialogTitle: String,
    taskToEdit: Task,
    todoViewModel: ProductivityViewModel
) {
    var name by remember { mutableStateOf(taskToEdit.name) }
    var categoriesText by remember { mutableStateOf(taskToEdit.categories.joinToString(", ")) }
    var content by remember { mutableStateOf(taskToEdit.content) } // <-- NUEVO: Estado para el contenido

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la tarea") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = categoriesText,
                    onValueChange = { categoriesText = it },
                    label = { Text("Categorías (separadas por coma)") },
                    modifier = Modifier.fillMaxWidth()
                )
                // --- NUEVO: Campo de texto para el contenido, solo para Ideas ---
                if (taskToEdit.type == TaskType.IDEA) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Descripción o notas") },
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                    )
                }
                // -------------------------------------------------------------
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedCategories = categoriesText
                        .split(',')
                        .map { it.trim() }
                        .filter { it.isNotBlank() }

                    val updatedTask = taskToEdit.copy(
                        name = name,
                        categories = updatedCategories,
                        content = content // <-- Guardamos el contenido
                    )
                    onEdit(updatedTask)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}