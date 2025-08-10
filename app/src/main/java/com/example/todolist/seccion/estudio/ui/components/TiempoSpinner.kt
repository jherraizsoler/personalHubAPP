package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiempoSpinner(label: String, value: Int, onValueChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    // Define la lista de tiempos disponibles
    val tiempos = listOf(1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = "$value min", // Muestra el valor seleccionado con "min"
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            tiempos.forEach { tiempo ->
                DropdownMenuItem(
                    text = { Text("$tiempo min") },
                    onClick = {
                        onValueChange(tiempo)
                        expanded = false
                    }
                )
            }
        }
    }
}