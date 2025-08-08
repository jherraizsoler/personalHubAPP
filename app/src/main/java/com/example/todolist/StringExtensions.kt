package com.example.todolist

/**
 * Función de extensión para la clase String que convierte una cadena de texto
 * con categorías separadas por comas en una lista de cadenas.
 *
 * Ejemplo: "trabajo, personal, urgente" se convierte en ["trabajo", "personal", "urgente"]
 */
fun String.toCategoryList(): List<String> {
    return this.split(',')
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct()
}