package com.example.todolist.seccion.estudio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModel
import com.example.todolist.seccion.estudio.viewmodel.EstudioViewModelFactory
import com.example.todolist.ui.theme.CustomTopAppBar

@Composable
fun EstudioScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Obtenemos el contexto y lo usamos para crear el ViewModel con el factory
    val context = LocalContext.current
    val estudioViewModel: EstudioViewModel = viewModel(
        factory = EstudioViewModelFactory(context)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            PomodoroTimer(
                tiempoRestante = estudioViewModel.tiempoRestante.value,
                isRunning = estudioViewModel.isRunning.value,
                isTrabajo = estudioViewModel.isTrabajo.value,
                onStartClick = estudioViewModel::iniciarTemporizador,
                onPauseClick = estudioViewModel::pausarTemporizador,
                onResetClick = estudioViewModel::reiniciarTemporizador
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Registros de Estudio",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(estudioViewModel.registros) { registro ->
                    RegistroEstudioItem(
                        registro = registro,
                        onEliminarClick = { estudioViewModel.eliminarRegistroEstudio(registro) }
                    )
                }
            }
        }
    }
}