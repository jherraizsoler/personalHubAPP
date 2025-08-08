package com.example.todolist.seccion.finanzas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions // <-- ¡Añade esta importación!
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType // <-- ¡Y esta otra importación!
import androidx.compose.ui.unit.dp
import com.example.todolist.seccion.finanzas.data.TransaccionCategoria
import com.example.todolist.seccion.finanzas.data.TransaccionTipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaccionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, TransaccionTipo, TransaccionCategoria) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TransaccionTipo.INGRESO) }
    var categoria by remember { mutableStateOf(TransaccionCategoria.SALARIO) }
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedCategoria by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Transacción") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth(),
                    // <-- La sintaxis correcta
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedTipo,
                    onExpandedChange = { expandedTipo = !expandedTipo }
                ) {
                    TextField(
                        readOnly = true,
                        value = tipo.name,
                        onValueChange = {},
                        label = { Text("Tipo") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedTipo
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTipo,
                        onDismissRequest = { expandedTipo = false }
                    ) {
                        TransaccionTipo.values().forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.name) },
                                onClick = {
                                    tipo = selectionOption
                                    expandedTipo = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedCategoria,
                    onExpandedChange = { expandedCategoria = !expandedCategoria }
                ) {
                    TextField(
                        readOnly = true,
                        value = categoria.name,
                        onValueChange = {},
                        label = { Text("Categoría") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedCategoria
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategoria,
                        onDismissRequest = { expandedCategoria = false }
                    ) {
                        TransaccionCategoria.values().forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.name) },
                                onClick = {
                                    categoria = selectionOption
                                    expandedCategoria = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val montoDouble = monto.toDoubleOrNull() ?: 0.0
                    onConfirm(nombre, montoDouble, tipo, categoria)
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}