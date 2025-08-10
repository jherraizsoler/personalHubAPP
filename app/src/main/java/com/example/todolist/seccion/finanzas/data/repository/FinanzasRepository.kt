package com.example.todolist.seccion.finanzas.data.repository

import android.content.Context
import com.example.todolist.data.loadEncryptedData
import com.example.todolist.data.saveEncryptedData
import com.example.todolist.seccion.finanzas.data.Transaccion

class FinanzasRepository(private val context: Context) {

    private val FILENAME = "finanzas.json"

    suspend fun loadTransacciones(): List<Transaccion> {
        return loadEncryptedData(context, FILENAME, emptyList())
    }

    suspend fun saveTransacciones(transacciones: List<Transaccion>) {
        saveEncryptedData(context, FILENAME, transacciones)
    }
}