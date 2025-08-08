package com.example.todolist.seccion.estudio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PomodoroTimer(
    tiempoRestante: Long,
    isRunning: Boolean,
    isTrabajo: Boolean,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutos = (tiempoRestante / 60_000).toString().padStart(2, '0')
    val segundos = ((tiempoRestante % 60_000) / 1000).toString().padStart(2, '0')
    val estado = if (isTrabajo) "Tiempo de Estudio" else "Tiempo de Descanso"

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = estado,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$minutos:$segundos",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = onStartClick, enabled = !isRunning) {
                    Text("Iniciar")
                }
                Button(onClick = onPauseClick, enabled = isRunning) {
                    Text("Pausar")
                }
                Button(onClick = onResetClick) {
                    Text("Reiniciar")
                }
            }
        }
    }
}