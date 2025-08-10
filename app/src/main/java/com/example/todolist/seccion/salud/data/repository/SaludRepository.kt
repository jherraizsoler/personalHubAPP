package com.example.todolist.seccion.salud.data.repository

import android.content.Context
import com.example.todolist.data.loadEncryptedData
import com.example.todolist.data.saveEncryptedData
import com.example.todolist.seccion.salud.data.SaludState

class SaludRepository(private val context: Context) {

    private val FILENAME = "salud.json"

    suspend fun loadEstado(): SaludState {
        return loadEncryptedData(context, FILENAME, SaludState(emptyList(), emptyList()))
    }

    suspend fun saveEstado(estado: SaludState) {
        saveEncryptedData(context, FILENAME, estado)
    }
}