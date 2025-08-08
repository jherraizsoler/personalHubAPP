package com.example.todolist.seccion.finanzas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.example.todolist.seccion.finanzas.data.Transaccion
import com.example.todolist.seccion.finanzas.data.TransaccionTipo
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun BalanceCard(balance: Double, modifier: Modifier = Modifier) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Balance Total",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currencyFormat.format(balance),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun RegistroItem(transaccion: Transaccion, onEliminarClick: () -> Unit, modifier: Modifier = Modifier) {
    val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    val color = when (transaccion.tipo) {
        TransaccionTipo.INGRESO -> MaterialTheme.colorScheme.tertiary
        TransaccionTipo.GASTO -> MaterialTheme.colorScheme.error
    }
    val simbolo = if (transaccion.tipo == TransaccionTipo.INGRESO) "+" else "-"
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "ES"))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaccion.nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${transaccion.categoria} - ${dateFormat.format(transaccion.fecha)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "$simbolo ${currencyFormat.format(transaccion.monto)}",
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = onEliminarClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}