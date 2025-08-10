package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todolist.seccion.estudio.data.Materia
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionSesionDialog(
    viewModel: EstudioViewModel,
    onDismiss: () -> Unit,
    onIniciarSesion: () -> Unit,
    onRestablecerContador: () -> Unit
) {
    var expandedMateria by remember { mutableStateOf(false) }
    var showAgregarMateriaDialog by remember { mutableStateOf(false) }
    // Nuevo estado para el diálogo de confirmación
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var materiaToDelete by remember { mutableStateOf<Materia?>(null) }

    val tiempos = listOf(1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60)

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Configuración de la Sesión",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                TiempoSpinner(
                    label = "Tiempo de Estudio",
                    value = viewModel.tiempoEstudio.value,
                    onValueChange = { viewModel.tiempoEstudio.value = it },
                    opciones = tiempos,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                TiempoSpinner(
                    label = "Tiempo de Descanso",
                    value = viewModel.tiempoDescanso.value,
                    onValueChange = { viewModel.tiempoDescanso.value = it },
                    opciones = tiempos,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

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
                        DropdownMenuItem(
                            text = { Text("Agregar nueva materia") },
                            onClick = {
                                showAgregarMateriaDialog = true
                                expandedMateria = false
                            },
                            trailingIcon = { Icon(Icons.Default.Add, contentDescription = "Agregar materia") }
                        )
                        Divider()
                        // Lista de materias existentes con el botón de eliminar
                        viewModel.materias.forEach { materia ->
                            DropdownMenuItem(
                                text = { Text(materia.nombre) },
                                onClick = {
                                    viewModel.materiaSeleccionada.value = materia
                                    expandedMateria = false
                                },
                                // Añadimos el botón de eliminar aquí
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            // 1. Guardar la materia a eliminar y mostrar el diálogo
                                            materiaToDelete = materia
                                            showConfirmDeleteDialog = true
                                            expandedMateria = false
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar materia")
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = { onDismiss() }) { Text("Cancelar") }
                    TextButton(onClick = { onRestablecerContador() }) { Text("Restablecer") }
                    TextButton(
                        onClick = { onIniciarSesion(); onDismiss() },
                        enabled = viewModel.materiaSeleccionada.value != null
                    ) { Text("Iniciar") }
                }
            }
        }
    }

    if (showAgregarMateriaDialog) {
        DialogoAgregarMateria(
            onDismiss = { showAgregarMateriaDialog = false },
            onAgregarMateria = { nombre, color ->
                viewModel.agregarMateria(nombre, color)
                showAgregarMateriaDialog = false
            }
        )
    }

    // Nuevo diálogo de confirmación para eliminar
    if (showConfirmDeleteDialog) {
        materiaToDelete?.let { materia ->
            AlertDialog(
                onDismissRequest = {
                    showConfirmDeleteDialog = false
                    materiaToDelete = null
                },
                title = { Text("Confirmar Eliminación") },
                text = { Text("¿Estás seguro de que quieres eliminar la materia '${materia.nombre}'?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.eliminarMateria(materia)
                            showConfirmDeleteDialog = false
                            materiaToDelete = null
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showConfirmDeleteDialog = false
                            materiaToDelete = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}