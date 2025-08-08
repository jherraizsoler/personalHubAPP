package com.example.todolist.seccion.finanzas.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FinanzasRepository {

    // Puedes añadir la lógica del repositorio aquí
    private val _items = MutableStateFlow(listOf("Ejemplo de gasto 1", "Ejemplo de ingreso 2"))
    val items: StateFlow<List<String>> = _items.asStateFlow()
}