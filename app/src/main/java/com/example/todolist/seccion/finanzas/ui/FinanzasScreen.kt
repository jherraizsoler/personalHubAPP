

package com.example.todolist.seccion.finanzas.ui

import com.example.todolist.seccion.finanzas.ui.BalanceCard
import com.example.todolist.seccion.finanzas.ui.RegistroItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.seccion.finanzas.viewmodel.FinanzasViewModel
import com.example.todolist.seccion.finanzas.data.TransaccionCategoria
import com.example.todolist.seccion.finanzas.data.TransaccionTipo
import com.example.todolist.ui.theme.CustomTopAppBar
import com.example.todolist.seccion.finanzas.ui.AddTransaccionDialog
import com.example.todolist.seccion.finanzas.viewmodel.FinanzasViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanzasScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val finanzasViewModel: FinanzasViewModel = viewModel(
        factory = FinanzasViewModelFactory(context)
    )

    val balance = finanzasViewModel.obtenerBalanceTotal()
    val transacciones = finanzasViewModel.transacciones
    var showAddDialog by remember { mutableStateOf(false) }

    // Usamos un Scaffold con una barra superior nula.
    // Esto permite que el Scaffold de MainActivity controle la TopAppBar
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Transacción")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BalanceCard(balance = balance)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(transacciones) { transaccion ->
                    RegistroItem(
                        transaccion = transaccion,
                        onEliminarClick = { finanzasViewModel.eliminarTransaccion(transaccion) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddTransaccionDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { nombre, monto, tipo, categoria ->
                finanzasViewModel.añadirTransaccion(nombre, monto, tipo, categoria)
                showAddDialog = false
            }
        )
    }
}