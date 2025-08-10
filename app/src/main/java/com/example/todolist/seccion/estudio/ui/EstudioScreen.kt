package com.example.todolist.seccion.estudio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.seccion.estudio.data.Materia
import com.example.todolist.seccion.estudio.ui.components.Calendar
import com.example.todolist.seccion.estudio.ui.components.ConfiguracionSesionDialog
import com.example.todolist.seccion.estudio.ui.components.DialogoSesionesDia
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModel
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModelFactory
import java.util.*

@Composable
fun EstudioScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: EstudioViewModel = viewModel(
        factory = EstudioViewModelFactory(context)
    )

    var showConfiguracionDialog by remember { mutableStateOf(false) }
    var showDialogoSesiones by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf(Date()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para abrir el diálogo de configuración de la sesión
        Button(
            onClick = { showConfiguracionDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Configurar Sesión")
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Temporizador (sin los botones ni spinners)
        Text(
            text = viewModel.estadoTemporizador.value,
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Botones de control del temporizador (opcionalmente puedes moverlos a otro sitio)
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

        Spacer(modifier = Modifier.height(5.dp))

        // Calendario

        Calendar(
            sessions = viewModel.registros,
            onDayClick = { date ->
                fechaSeleccionada = date
                showDialogoSesiones = true
            }
        )
    }

    if (showConfiguracionDialog) {
        ConfiguracionSesionDialog(
            viewModel = viewModel,
            onDismiss = { showConfiguracionDialog = false },
            onIniciarSesion = { /* Aquí va la lógica para iniciar la sesión */ },
            onRestablecerContador = { /* Aquí va la lógica para restablecer el contador */ }
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