package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todolist.seccion.estudio.data.Materia
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionSesionDialog(
    viewModel: EstudioViewModel,
    onDismiss: () -> Unit
) {
    var expandedTiempoEstudio by remember { mutableStateOf(false) }
    var expandedTiempoDescanso by remember { mutableStateOf(false) }
    var expandedMateria by remember { mutableStateOf(false) }
    var showAgregarMateriaDialog by remember { mutableStateOf(false) }

    val tiempos = listOf(1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60)

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Configuración de Sesión",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Spinner para el tiempo de estudio
                ExposedDropdownMenuBox(
                    expanded = expandedTiempoEstudio,
                    onExpandedChange = { expandedTiempoEstudio = !expandedTiempoEstudio },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    TextField(
                        readOnly = true,
                        value = "${viewModel.tiempoEstudio.value} min",
                        onValueChange = {},
                        label = { Text("Tiempo de Estudio") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTiempoEstudio) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTiempoEstudio,
                        onDismissRequest = { expandedTiempoEstudio = false }
                    ) {
                        tiempos.forEach { tiempo ->
                            DropdownMenuItem(
                                text = { Text("$tiempo min") },
                                onClick = {
                                    viewModel.tiempoEstudio.value = tiempo
                                    expandedTiempoEstudio = false
                                }
                            )
                        }
                    }
                }

                // Spinner para el tiempo de descanso
                ExposedDropdownMenuBox(
                    expanded = expandedTiempoDescanso,
                    onExpandedChange = { expandedTiempoDescanso = !expandedTiempoDescanso },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    TextField(
                        readOnly = true,
                        value = "${viewModel.tiempoDescanso.value} min",
                        onValueChange = {},
                        label = { Text("Tiempo de Descanso") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTiempoDescanso) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTiempoDescanso,
                        onDismissRequest = { expandedTiempoDescanso = false }
                    ) {
                        tiempos.forEach { tiempo ->
                            DropdownMenuItem(
                                text = { Text("$tiempo min") },
                                onClick = {
                                    viewModel.tiempoDescanso.value = tiempo
                                    expandedTiempoDescanso = false
                                }
                            )
                        }
                    }
                }

                // Spinner para la materia con botones de agregar y eliminar
                ExposedDropdownMenuBox(
                    expanded = expandedMateria,
                    onExpandedChange = { expandedMateria = !expandedMateria },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    TextField(
                        readOnly = true,
                        value = viewModel.materiaSeleccionada.value?.nombre ?: "Selecciona una materia",
                        onValueChange = {},
                        label = { Text("Materia") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMateria) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMateria,
                        onDismissRequest = { expandedMateria = false }
                    ) {
                        // Botón para agregar nueva materia
                        DropdownMenuItem(
                            text = { Text("Agregar nueva materia") },
                            onClick = {
                                showAgregarMateriaDialog = true
                                expandedMateria = false
                            },
                            trailingIcon = { Icon(Icons.Default.Add, contentDescription = "Agregar materia") }
                        )
                        Divider()
                        // Lista de materias con botón de eliminar
                        viewModel.materias.forEach { materia ->
                            DropdownMenuItem(
                                text = { Text(materia.nombre) },
                                onClick = {
                                    viewModel.materiaSeleccionada.value = materia
                                    expandedMateria = false
                                },
                                trailingIcon = {
                                    IconButton(onClick = { viewModel.eliminarMateria(materia.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar materia")
                                    }
                                }
                            )
                        }
                    }
                }

                Button(onClick = { onDismiss() }) {
                    Text("Guardar y cerrar")
                }
            }
        }
    }

    if (showAgregarMateriaDialog) {
        DialogoAgregarMateria(
            onDismiss = { showAgregarMateriaDialog = false },
            onAgregarMateria = { nombre ->
                viewModel.añadirMateria(nombre)
                showAgregarMateriaDialog = false
            }
        )
    }
}