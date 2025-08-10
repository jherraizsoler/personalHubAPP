package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todolist.seccion.estudio.data.RegistroEstudio
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RegistroEstudioItem(
    sesion: RegistroEstudio,
    viewModel: EstudioViewModel,
    onEliminar: (RegistroEstudio) -> Unit
) {
    val materia = viewModel.obtenerMateriaPorId(sesion.materiaId)
    val materiaColor = materia?.let { Color(it.color) } ?: Color.LightGray

    val horaInicioZoned = Instant.ofEpochMilli(sesion.horaInicio).atZone(ZoneId.systemDefault())
    val horaFinZoned = Instant.ofEpochMilli(sesion.horaFin).atZone(ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    val horaInicio = formatter.format(horaInicioZoned)
    val horaFin = formatter.format(horaFinZoned)

    // He cambiado el Card por un Box con un fondo para poder aplicar el color
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(materiaColor)
    ) {
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
                Text("De $horaInicio a $horaFin", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onEliminar(sesion) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar sesión")
            }
        }
    }
}