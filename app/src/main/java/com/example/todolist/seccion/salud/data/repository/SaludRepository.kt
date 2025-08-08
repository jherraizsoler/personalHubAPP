package com.example.todolist.seccion.salud.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SaludRepository {

    // Lógica del repositorio para la sección de salud
    private val _items = MutableStateFlow(listOf("Beber 2 litros de agua", "Hacer ejercicio"))
    val items: StateFlow<List<String>> = _items.asStateFlow()
}
