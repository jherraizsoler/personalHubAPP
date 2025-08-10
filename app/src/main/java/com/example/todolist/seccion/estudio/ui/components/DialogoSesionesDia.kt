package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
                            // Aquí se llama al componente RegistroEstudioItem.
                            RegistroEstudioItem(
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