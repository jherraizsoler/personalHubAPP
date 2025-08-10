package com.example.todolist.data

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

val json = Json { prettyPrint = true }
val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

/**
 * Cifra y guarda un objeto en un archivo JSON seguro.
 */
inline suspend fun <reified T : Any> saveEncryptedData(context: Context, filename: String, data: T) {
    val file = File(context.filesDir, filename)
    val encryptedFile = EncryptedFile.Builder(
        file,
        context,
        masterKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    try {
        withContext(Dispatchers.IO) {
            val jsonString = json.encodeToString(data)
            encryptedFile.openFileOutput().bufferedWriter().use { writer ->
                writer.write(jsonString)
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

/**
 * Carga y descifra datos desde un archivo JSON seguro.
 */
inline suspend fun <reified T : Any> loadEncryptedData(context: Context, filename: String, defaultValue: T): T {
    val file = File(context.filesDir, filename)

    if (!file.exists()) {
        saveEncryptedData(context, filename, defaultValue)
        return defaultValue
    }

    val encryptedFile = EncryptedFile.Builder(
        file,
        context,
        masterKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    return try {
        withContext(Dispatchers.IO) {
            val inputStream = encryptedFile.openFileInput()
            val byteArrayOutputStream = ByteArrayOutputStream()
            var nextByte = inputStream.read()
            while (nextByte != -1) {
                byteArrayOutputStream.write(nextByte)
                nextByte = inputStream.read()
            }
            val jsonString = byteArrayOutputStream.toString("UTF-8")
            json.decodeFromString<T>(jsonString)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        saveEncryptedData(context, filename, defaultValue)
        defaultValue
    }
}