package com.example.todolist.seccion.salud.data

import kotlinx.serialization.Serializable
import java.util.UUID

// Clase contenedor para guardar todo el estado de la sección de Salud
@Serializable
data class SaludState(
    val pastillas: List<Pastilla>,
    val registrosAlimentacion: List<RegistroAlimentacion>,
    val analisis: List<Analisis> = emptyList() // He añadido Analisis para que no se pierda
)

@Serializable
data class Pastilla(
    val id: String = UUID.randomUUID().toString(), // Cambiamos UUID a String para serialización
    val nombre: String,
    val dosis: String,
    val frecuencia: String, // Ej: "8 horas" o "Diario"
    val ultimoRecordatorio: Long? = null
)

@Serializable
data class Analisis(
    val id: String = UUID.randomUUID().toString(), // Cambiamos UUID a String
    val nombre: String,
    val fecha: Long,
    val notas: String
)

@Serializable
data class RegistroAlimentacion(
    val id: String = UUID.randomUUID().toString(), // Cambiamos UUID a String
    val comida: String,
    val fecha: Long,
    val calorias: Int,
    val notas: String
)