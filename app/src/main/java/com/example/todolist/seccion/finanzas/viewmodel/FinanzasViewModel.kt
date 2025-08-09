package com.example.todolist.seccion.finanzas.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.loadData
import com.example.todolist.data.saveData
import com.example.todolist.seccion.finanzas.data.Transaccion
import com.example.todolist.seccion.finanzas.data.TransaccionCategoria
import com.example.todolist.seccion.finanzas.data.TransaccionTipo
import kotlinx.coroutines.launch

class FinanzasViewModel(private val context: Context) : ViewModel() { // <-- Recibimos el context

    private val FILENAME = "finanzas.json"

    private val _transacciones = mutableStateListOf<Transaccion>()
    val transacciones: List<Transaccion> = _transacciones

    init {
        // Al inicializar, cargamos los datos del archivo
        cargarTransacciones()
    }

    private fun cargarTransacciones() {
        viewModelScope.launch {
            val loadedTransactions = loadData(context, FILENAME, emptyList<Transaccion>())
            _transacciones.clear()
            _transacciones.addAll(loadedTransactions)
        }
    }

    private fun guardarTransacciones() {
        viewModelScope.launch {
            // CORRECCIÓN: Convierte la SnapshotStateList a una List antes de guardarla.
            saveData(context, FILENAME, _transacciones.toList())
        }
    }

    fun añadirTransaccion(
        nombre: String,
        monto: Double,
        tipo: TransaccionTipo,
        categoria: TransaccionCategoria
    ) {
        val nuevaTransaccion = Transaccion(
            nombre = nombre,
            monto = monto,
            tipo = tipo,
            categoria = categoria.toString(),
            fecha = System.currentTimeMillis()
        )
        _transacciones.add(nuevaTransaccion)
        guardarTransacciones()
    }

    fun eliminarTransaccion(transaccion: Transaccion) {
        _transacciones.remove(transaccion)
        guardarTransacciones()
    }

    fun obtenerBalanceTotal(): Double {
        val ingresos = _transacciones.filter { it.tipo == TransaccionTipo.INGRESO }.sumOf { it.monto }
        val gastos = _transacciones.filter { it.tipo == TransaccionTipo.GASTO }.sumOf { it.monto }
        return ingresos - gastos
    }
}