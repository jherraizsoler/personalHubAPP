package com.example.todolist.seccion.estudio.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.loadData
import com.example.todolist.data.saveData
import com.example.todolist.seccion.estudio.data.RegistroEstudio
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

class EstudioViewModel(private val context: Context) : ViewModel() {

    private val FILENAME = "estudio.json"

    private val _registros = mutableStateListOf<RegistroEstudio>()
    val registros: List<RegistroEstudio> = _registros

    private var timerJob: Job? = null
    private val _tiempoRestante = mutableStateOf(25 * 60L)
    val tiempoRestante: State<Long> = _tiempoRestante
    private val _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning
    private val _isTrabajo = mutableStateOf(true)
    val isTrabajo: State<Boolean> = _isTrabajo

    init {
        cargarRegistros()
    }

    private fun cargarRegistros() {
        viewModelScope.launch {
            val loadedRecords = loadData<List<RegistroEstudio>>(context, FILENAME)
            loadedRecords?.let {
                _registros.addAll(it)
            }
        }
    }

    private fun guardarRegistros() {
        viewModelScope.launch {
            saveData(context, FILENAME, _registros)
        }
    }

    fun iniciarTemporizador() {
        if (!isRunning.value) {
            _isRunning.value = true
            timerJob = viewModelScope.launch {
                while (_tiempoRestante.value > 0) {
                    delay(1000)
                    _tiempoRestante.value--
                }
                onTemporizadorTerminado()
            }
        }
    }

    fun pausarTemporizador() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun reiniciarTemporizador() {
        pausarTemporizador()
        _tiempoRestante.value = if (_isTrabajo.value) 25 * 60L else 5 * 60L
    }

    private fun onTemporizadorTerminado() {
        pausarTemporizador()
        if (_isTrabajo.value) {
            añadirRegistroEstudio(25, "Sesión de estudio")
            _isTrabajo.value = false
            _tiempoRestante.value = 5 * 60L
        } else {
            _isTrabajo.value = true
            _tiempoRestante.value = 25 * 60L
        }
    }

    fun añadirRegistroEstudio(duracionMinutos: Int, notas: String) {
        val nuevoRegistro = RegistroEstudio(
            fecha = System.currentTimeMillis(),
            duracionMinutos = duracionMinutos,
            notas = notas
        )
        _registros.add(nuevoRegistro)
        guardarRegistros()
    }

    fun eliminarRegistroEstudio(registro: RegistroEstudio) {
        _registros.remove(registro)
        guardarRegistros()
    }
}