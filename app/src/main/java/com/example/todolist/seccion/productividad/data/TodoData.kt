package com.example.todolist.seccion.productividad.data

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val content: String = "", // <-- NUEVA PROPIEDAD para el contenido
    val isCompleted: Boolean = false,
    val isDeleted: Boolean = false,
    val categories: List<String> = emptyList(),
    val type: TaskType = TaskType.TASK,
    val createdAt: String = Instant.now().toString(),
    val completedAt: String? = null,
    val deletedAt: String? = null
)

@Serializable
enum class TaskType {
    TASK,
    IDEA
}