package com.example.todolist.seccion.estudio.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.seccion.estudio.data.Materia
import com.example.todolist.seccion.estudio.data.RegistroEstudio
import com.example.todolist.seccion.estudio.ui.components.DialogoSesionesDia
import com.example.todolist.seccion.estudio.ui.components.TiempoSpinner
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModel
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudioScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: EstudioViewModel = viewModel(
        factory = EstudioViewModelFactory(context)
    )

    var showDialogoSesiones by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf(Date()) }
    var expandedMateria by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Spinners para tiempo de estudio, descanso y materia
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TiempoSpinner(
                label = "Estudio (min)",
                value = viewModel.tiempoEstudio.value,
                onValueChange = { viewModel.tiempoEstudio.value = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TiempoSpinner(
                label = "Descanso (min)",
                value = viewModel.tiempoDescanso.value,
                onValueChange = { viewModel.tiempoDescanso.value = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Spinner para seleccionar la materia
        ExposedDropdownMenuBox(
            expanded = expandedMateria,
            onExpandedChange = { expandedMateria = !expandedMateria },
            modifier = Modifier.fillMaxWidth()
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
                viewModel.materias.forEach { materia ->
                    DropdownMenuItem(
                        text = { Text(materia.nombre) },
                        onClick = {
                            viewModel.materiaSeleccionada.value = materia
                            expandedMateria = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Temporizador
        Text(
            text = viewModel.estadoTemporizador.value,
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de control del temporizador
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (viewModel.isTimerRunning.value) {
                        viewModel.pausarTemporizador()
                    } else {
                        viewModel.iniciarTemporizador(viewModel.materiaSeleccionada.value) {}
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (viewModel.isTimerRunning.value) "Pausar" else "Iniciar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.reiniciarTemporizador() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reiniciar")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Calendario con visualización profesional
        Text("Registros por Día", style = MaterialTheme.typography.titleLarge)

        // Uso del componente de calendario (puedes reemplazarlo por una librería si lo prefieres)
        Calendar(
            sessions = viewModel.registros,
            onDayClick = { date ->
                fechaSeleccionada = date
                showDialogoSesiones = true
            }
        )
    }

    if (showDialogoSesiones) {
        DialogoSesionesDia(
            viewModel = viewModel,
            fecha = fechaSeleccionada,
            onDismiss = { showDialogoSesiones = false }
        )
    }
}

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
            val daysOfWeek = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
            daysOfWeek.forEach { day ->
                Text(text = day, modifier = Modifier.size(40.dp), textAlign = TextAlign.Center)
            }
        }

        // Cuadrícula de días
        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfMonth = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        val startDayOffset = (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Ajuste para que Lunes sea el primer día (0-index)

        var dayCount = 1
        Column {
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