package com.example.todolist.seccion.estudio.data

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import java.util.UUID

// Se hace serializable para que el ViewModel pueda guardarla
@Serializable
data class Materia(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val color: Long = Color.LightGray.value.toLong() // Color claro por defecto
)

@Serializable
data class RegistroEstudio(
    val id: String = java.util.UUID.randomUUID().toString(),
    val materiaId: String,
    val duracionMinutos: Int,
    val notas: String,
    val horaInicio: Long,
    val horaFin: Long
)