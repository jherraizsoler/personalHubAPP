package com.example.todolist.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.io.IOException

val json = Json { prettyPrint = true }

/**
 * Guarda un objeto en un archivo JSON en la memoria interna de la app.
 */
inline suspend fun <reified T : Any> saveData(context: Context, filename: String, data: T) {
    val file = File(context.filesDir, filename)
    try {
        withContext(Dispatchers.IO) {
            val jsonString = json.encodeToString(data)
            file.writeText(jsonString)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

/**
 * Carga datos desde un archivo JSON.
 * Si no existe, crea el archivo con el valor por defecto y lo devuelve.
 */
inline suspend fun <reified T : Any> loadData(context: Context, filename: String, defaultValue: T): T {
    val file = File(context.filesDir, filename)

    // Si el archivo no existe, crearlo con el valor por defecto
    if (!file.exists()) {
        saveData(context, filename, defaultValue)
        return defaultValue
    }

    return try {
        withContext(Dispatchers.IO) {
            val jsonString = file.readText()
            json.decodeFromString<T>(jsonString)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        defaultValue
    }
}