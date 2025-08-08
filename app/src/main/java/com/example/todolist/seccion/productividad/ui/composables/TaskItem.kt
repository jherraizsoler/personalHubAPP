
// Archivo: TaskItem.kt
package com.example.todolist.seccion.productividad.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todolist.seccion.productividad.data.Task
import com.example.todolist.seccion.productividad.data.TaskType
import com.halilibo.richtext.markdown.Markdown


import com.halilibo.richtext.ui.RichText

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Task,
    onToggleTask: (Task) -> Unit,
    onArchiveTask: (Task) -> Unit,
    onEditClick: (Task) -> Unit,
    onShowCategoriesClick: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColors = if (task.isCompleted) {
        CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    } else {
        CardDefaults.cardColors()
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = cardColors,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggleTask(task) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.name,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.combinedClickable(
                            onClick = { onEditClick(task) },
                            onLongClick = { /* Opcional: Lógica para clic largo en el nombre */ }
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (task.categories.isNotEmpty()) {
                        IconButton(onClick = { onShowCategoriesClick(task.categories) }) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Ver categorías de la tarea"
                            )
                        }
                    }
                    IconButton(onClick = { onArchiveTask(task) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Archivar tarea",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            // --- CÓDIGO ACTUALIZADO: Usamos RichText y Markdown para el contenido ---
            if (task.type == TaskType.IDEA && task.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                RichText(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Markdown(content = task.content)
                }
            }
            // -----------------------------------------------------------------------
        }
    }
}