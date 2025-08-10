package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogoAgregarMateria(
    onDismiss: () -> Unit,
    onAgregarMateria: (String, Long) -> Unit // La firma ha sido actualizada a ULong
) {
    var nombreMateria by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color.LightGray) }

    // Colores claros para elegir
    val lightColors = listOf(
        Color(0xFFB3E5FC), Color(0xFFC8E6C9), Color(0xFFFFF9C4),
        Color(0xFFFFCDD2), Color(0xFFF0F4C3), Color(0xFFE1BEE7)
    )

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Agregar Nueva Materia", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = nombreMateria,
                    onValueChange = { nombreMateria = it },
                    label = { Text("Nombre de la materia") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Seleccionar color:", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // Selector de colores simple
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    lightColors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 2.dp,
                                    color = if (selectedColor == color) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            if (nombreMateria.isNotBlank()) {
                                // Se pasa el ULong, no el Long
                                onAgregarMateria(nombreMateria, selectedColor.toArgb().toLong())
                                onDismiss()
                            }
                        },
                        enabled = nombreMateria.isNotBlank()
                    ) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}