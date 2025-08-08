package com.example.todolist.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.io.IOException

// Usamos el 'Json' object para la serialización y deserialización
val json = Json { prettyPrint = true }

// Guarda los datos en un archivo JSON
inline suspend fun <reified T : Any> saveData(context: Context, filename: String, data: T) {
    val file = File(context.filesDir, filename)
    try {
        withContext(Dispatchers.IO) {
            // Serializa el objeto a String
            val jsonString = json.encodeToString(data)
            // Escribe el String en el archivo
            file.writeText(jsonString)
        }
    } catch (e: IOException) {
        // Manejar errores de escritura de archivo
        e.printStackTrace()
    }
}

// Carga los datos desde un archivo JSON
inline suspend fun <reified T : Any> loadData(context: Context, filename: String): T? {
    val file = File(context.filesDir, filename)
    if (!file.exists()) {
        return null
    }
    return try {
        withContext(Dispatchers.IO) {
            // Lee el contenido del archivo
            val jsonString = file.readText()
            // Deserializa el String a un objeto
            json.decodeFromString<T>(jsonString)
        }
    } catch (e: Exception) {
        // Manejar errores de lectura o deserialización
        e.printStackTrace()
        null
    }
}