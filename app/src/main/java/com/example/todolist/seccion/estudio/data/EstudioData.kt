package com.example.todolist.seccion.estudio.data

import kotlinx.serialization.Serializable
import java.util.UUID

// Se hace serializable para que el ViewModel pueda guardarla
@Serializable
data class Materia(
    val id: String = UUID.randomUUID().toString(), // Identificador único para gestionar la materia
    val nombre: String,
    val color: Long // Se usa Long para almacenar el color de Compose (ARGB)
)

@Serializable
data class RegistroEstudio(
    val id: String = UUID.randomUUID().toString(),
    val fecha: Long,
    val materiaId: String, // Referencia a la materia seleccionada por su ID
    val duracionMinutos: Int,
    val notas: String
)