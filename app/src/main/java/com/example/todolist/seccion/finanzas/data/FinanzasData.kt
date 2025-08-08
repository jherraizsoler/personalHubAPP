package com.example.todolist.seccion.finanzas.data

import java.util.UUID

data class Transaccion(
    val id: UUID = UUID.randomUUID(),
    val nombre: String,
    val monto: Double,
    val tipo: TransaccionTipo,
    val categoria: String,
    val fecha: Long = System.currentTimeMillis()
)

data class CategoriaFinanzas(
    val nombre: String,
    val color: Int // Usar un Int para representar un Color en Compose
)

enum class TransaccionTipo {
    INGRESO,
    GASTO
}

enum class TransaccionCategoria {
    SALARIO,
    INVERSION,
    GASTO_FIJO,
    GASTO_VARIABLE,
    OCIO
}