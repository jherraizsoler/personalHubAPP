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
    val sessionDates = sessions.map { dateFormat.format(Date(it.horaInicio)) }.toSet()

    val azulOscuro = Color(0xFF244259)

    Spacer(modifier = Modifier.height(8.dp))

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) } }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mes anterior")
            }
            // SimpleDateFormat con Locale("es") ya capitaliza la primera letra del mes.
            Text(
                text = "Sesiones \n" +  SimpleDateFormat("MMMM yyyy", Locale("es")).format(currentMonth.time).capitalizeFirstLetter(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) } }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mes siguiente")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            val daysOfWeek = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
            daysOfWeek.forEach { day ->
                Text(text = day, modifier = Modifier.size(40.dp), textAlign = TextAlign.Center)
            }
        }

        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfMonth = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        val startDayOffset = (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) + 5) % 7

        var dayCount = 1
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy((-4).dp)
        ){
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
                                    .background(if (hasSession) azulOscuro else Color.Transparent)
                                    .clickable { onDayClick(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (hasSession) Color.White else MaterialTheme.colorScheme.onBackground
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

// Función de extensión para capitalizar la primera letra de un string.
fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}