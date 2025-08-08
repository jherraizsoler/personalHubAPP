package com.example.todolist.seccion.estudio.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RegistroEstudio(
    val id: String = UUID.randomUUID().toString(),
    val fecha: Long,
    val duracionMinutos: Int, // <-- Revisa este nombre
    val notas: String // <-- Y este nombre
)
data class Materia(
    val nombre: String,
    val color: Int // Int para representar un color en Compose
)