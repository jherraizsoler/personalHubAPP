// Archivo: AddDialog.kt
package com.example.todolist.seccion.productividad.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.seccion.productividad.data.TaskType
import com.example.todolist.seccion.productividad.viewmodel.ProductivityViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(
    onDismiss: () -> Unit,
    onAdd: (String, List<String>, String) -> Unit, // <-- Añadido nuevo parámetro para el contenido
    dialogTitle: String,
    labelName: String,
    taskType: TaskType, // <-- NUEVO: Recibe el tipo de tarea para mostrar el campo condicionalmente
    todoViewModel: ProductivityViewModel
) {
    var name by remember { mutableStateOf("") }
    var categoriesText by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") } // <-- NUEVO: Estado para el contenido

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(labelName) },
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
                if (taskType == TaskType.IDEA) {
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
                    val categoriesToAdd = categoriesText
                        .split(',')
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                    onAdd(name, categoriesToAdd, content) // <-- Pasamos el contenido
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}