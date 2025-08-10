package com.example.todolist.seccion.estudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todolist.seccion.estudio.data.RegistroEstudio
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Calendar(sessions: List<RegistroEstudio>, onDayClick: (Date) -> Unit) {
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val sessionDates = sessions.map { dateFormat.format(Date(it.fecha)) }.toSet()

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // Encabezado del mes con botones de navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.apply { add(Calendar.MONTH, -1) } }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mes anterior")
            }
            Text(
                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.time),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { currentMonth = currentMonth.apply { add(Calendar.MONTH, 1) } }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mes siguiente")
            }
        }

        // Días de la semana
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            val daysOfWeek = listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
            daysOfWeek.forEach { day ->
                Text(text = day, modifier = Modifier.size(40.dp), textAlign = TextAlign.Center)
            }
        }

        // Cuadrícula de días
        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfMonth = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        val startDayOffset = (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Ajuste para que Lunes sea el primer día

        Column {
            var dayCount = 1
            for (week in 0 until 6) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    for (dayOfWeek in 0 until 7) {
                        if ((week == 0 && dayOfWeek < startDayOffset) || dayCount > daysInMonth) {
                            Spacer(modifier = Modifier.size(40.dp))
                        } else {
                            val day = dayCount
                            val date = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, day) }.time
                            val dateString = dateFormat.format(date)
                            val hasSession = sessionDates.contains(dateString)

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (hasSession) Color.Blue else Color.Transparent)
                                    .clickable { onDayClick(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = Color.Black
                                )
                            }
                            dayCount++
                        }
                    }
                }
            }
        }
    }
}