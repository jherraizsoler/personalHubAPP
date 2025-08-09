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

class EstudioViewModel(private val context: Context) : ViewModel() {

    private val FILENAME = "estudio.json"

    private val _registros = mutableStateListOf<RegistroEstudio>()
    val registros: List<RegistroEstudio> = _registros

    private var timerJob: Job? = null

    private val _tiempoRestante = mutableStateOf(25 * 60_000L)
    val tiempoRestante: State<Long> = _tiempoRestante

    private val _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning

    private val _isTrabajo = mutableStateOf(true)
    val isTrabajo: State<Boolean> = _isTrabajo

    private var tiempoInicialSesion = 25 * 60_000L // para calcular sesiones parciales

    init {
        cargarRegistros()
    }

    private fun cargarRegistros() {
        viewModelScope.launch {
            val loadedRecords = loadData(context, FILENAME, emptyList<RegistroEstudio>())
            _registros.clear()
            _registros.addAll(loadedRecords)
        }
    }

    private fun guardarRegistros() {
        viewModelScope.launch {
            // Convierte el SnapshotStateList a una List antes de guardarlo.
            saveData(context, FILENAME, _registros.toList())
        }
    }

    fun iniciarTemporizador() {
        if (!isRunning.value) {
            _isRunning.value = true
            tiempoInicialSesion = _tiempoRestante.value
            timerJob = viewModelScope.launch {
                while (_tiempoRestante.value > 0 && _isRunning.value) {
                    delay(1000)
                    _tiempoRestante.value -= 1000
                }
                if (_isRunning.value) {
                    onTemporizadorTerminado()
                }
            }
        }
    }

    fun pausarTemporizador() {
        if (_isRunning.value) {
            // Si es trabajo y no hemos pausado en el segundo 0
            if (_isTrabajo.value && _tiempoRestante.value < tiempoInicialSesion) {
                val minutosTrabajados =
                    ((tiempoInicialSesion - _tiempoRestante.value) / 60_000).toInt()
                if (minutosTrabajados > 0) {
                    añadirRegistroEstudio(minutosTrabajados, "Sesión interrumpida")
                }
            }
        }
        _isRunning.value = false
        timerJob?.cancel()
        guardarRegistros()
    }

    fun reiniciarTemporizador() {
        pausarTemporizador()
        _tiempoRestante.value = if (_isTrabajo.value) 25 * 60_000L else 5 * 60_000L
    }

    private fun onTemporizadorTerminado() {
        pausarTemporizador()
        if (_isTrabajo.value) {
            añadirRegistroEstudio(25, "Sesión de estudio")
            _isTrabajo.value = false
            _tiempoRestante.value = 5 * 60_000L
        } else {
            _isTrabajo.value = true
            _tiempoRestante.value = 25 * 60_000L
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