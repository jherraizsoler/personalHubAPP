package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todolist.seccion.estudio.data.RegistroEstudio
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DialogoSesionesDia(
    viewModel: EstudioViewModel,
    fecha: Date,
    onDismiss: () -> Unit
) {
    val sesionesDelDia = viewModel.obtenerSesionesPorFecha(fecha)
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Sesiones del día: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha)}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (sesionesDelDia.isEmpty()) {
                    Text("No hay sesiones registradas para este día.", modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(sesionesDelDia) { sesion ->
                            SesionItem(
                                sesion = sesion,
                                viewModel = viewModel,
                                onEliminar = { viewModel.eliminarRegistro(it) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { onDismiss() }, modifier = Modifier.align(Alignment.End)) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
fun SesionItem(sesion: RegistroEstudio, viewModel: EstudioViewModel, onEliminar: (RegistroEstudio) -> Unit) {
    val materia = viewModel.obtenerMateriaPorId(sesion.materiaId)
    val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(sesion.fecha))

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = materia?.nombre ?: "Materia desconocida",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Duración: ${sesion.duracionMinutos} min", style = MaterialTheme.typography.bodyMedium)
                Text("Hora: $hora", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onEliminar(sesion) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar sesión")
            }
        }
    }
}