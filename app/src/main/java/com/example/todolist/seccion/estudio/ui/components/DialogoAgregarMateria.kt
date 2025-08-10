package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun DialogoAgregarMateria(
    onDismiss: () -> Unit,
    onAgregarMateria: (String) -> Unit
) {
    var nombreMateria by remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Agregar Nueva Materia", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = nombreMateria,
                    onValueChange = { nombreMateria = it },
                    label = { Text("Nombre de la materia") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        if (nombreMateria.isNotBlank()) {
                            onAgregarMateria(nombreMateria)
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}