package com.example.todolist.seccion.productividad.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todolist.seccion.productividad.data.Task
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryItemCard(
    item: Task,
    onRestore: () -> Unit,
    onPermanentlyDelete: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Mostrar fechas según el estado
                val createdAtDate = Instant.parse(item.createdAt).atZone(ZoneId.systemDefault()).toLocalDate()
                Text("Creada: ${dateFormatter.format(createdAtDate)}", style = MaterialTheme.typography.bodySmall)

                item.completedAt?.let { completedAt ->
                    val completedAtDate = Instant.parse(completedAt).atZone(ZoneId.systemDefault()).toLocalDate()
                    Text("Completada: ${dateFormatter.format(completedAtDate)}", style = MaterialTheme.typography.bodySmall)
                }

                item.deletedAt?.let { deletedAt ->
                    val deletedAtDate = Instant.parse(deletedAt).atZone(ZoneId.systemDefault()).toLocalDate()
                    Text("Eliminada: ${dateFormatter.format(deletedAtDate)}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (item.isDeleted) {
                    IconButton(onClick = onRestore) {
                        Icon(
                            imageVector = Icons.Default.Restore,
                            contentDescription = "Restaurar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                IconButton(onClick = onPermanentlyDelete) {
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