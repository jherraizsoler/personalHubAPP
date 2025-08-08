package com.example.todolist.seccion.productividad.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todolist.seccion.productividad.data.Task

@Composable
fun DeletedItem(
    item: Task,
    onRestore: (Task) -> Unit,
    onPermanentDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                textDecoration = TextDecoration.LineThrough,
                style = MaterialTheme.typography.bodyLarge
            )
            Row {
                IconButton(onClick = { onRestore(item) }) {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = "Restaurar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onPermanentDelete(item) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar permanentemente",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}