package com.example.todolist.seccion.estudio.data.repository

import android.content.Context
import com.example.todolist.data.loadEncryptedData
import com.example.todolist.data.saveEncryptedData
import com.example.todolist.seccion.estudio.data.Materia
import com.example.todolist.seccion.estudio.data.RegistroEstudio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class EstudioRepository(private val context: Context) {

    private val REGISTROS_FILENAME = "estudio_sessions.json"
    private val MATERIAS_FILENAME = "estudio_materias.json"

    // --- Funciones para Materias ---

    suspend fun loadMaterias(): List<Materia> {
        return loadEncryptedData(context, MATERIAS_FILENAME, emptyList())
    }

    suspend fun saveMaterias(materias: List<Materia>) {
        saveEncryptedData(context, MATERIAS_FILENAME, materias)
    }

    suspend fun getMateriaById(materiaId: String): Materia? {
        val materias = loadMaterias()
        return materias.find { it.id == materiaId }
    }

    // --- Funciones para Registros ---

    suspend fun loadRegistros(): List<RegistroEstudio> {
        return loadEncryptedData(context, REGISTROS_FILENAME, emptyList())
    }

    suspend fun saveRegistros(registros: List<RegistroEstudio>) {
        saveEncryptedData(context, REGISTROS_FILENAME, registros)
    }

    suspend fun getSesionesByDate(fecha: Date): List<RegistroEstudio> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaString = dateFormat.format(fecha)
        val registros = loadRegistros()
        return registros.filter { dateFormat.format(Date(it.horaInicio)) == fechaString }
    }
}