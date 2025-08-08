package com.example.todolist.seccion.estudio.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EstudioRepository {

    // Lógica del repositorio para la sección de estudio
    private val _items = MutableStateFlow(listOf("Matemáticas", "Historia"))
    val items: StateFlow<List<String>> = _items.asStateFlow()
}