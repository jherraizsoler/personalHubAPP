
package com.example.todolist.seccion.estudio.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.loadData
import com.example.todolist.data.saveData
import com.example.todolist.seccion.estudio.data.Materia
import com.example.todolist.seccion.estudio.data.RegistroEstudio
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class EstudioViewModel(private val context: Context) : ViewModel() {

    private val REGISTROS_FILENAME = "estudio_sessions.json"
    private val MATERIAS_FILENAME = "estudio_materias.json"

    // Estados para los spinners y el temporizador
    val tiempoEstudio = mutableStateOf(25)
    val tiempoDescanso = mutableStateOf(5)
    val numeroSesiones = mutableStateOf(1)
    val numeroDescansos = mutableStateOf(0)

    val estadoTemporizador = mutableStateOf("00:00")
    val isTimerRunning = mutableStateOf(false)
    val materiaSeleccionada = mutableStateOf<Materia?>(null)

    // Lista de materias gestionada y persistente
    private val _materias = mutableStateListOf<Materia>()
    val materias: List<Materia> = _materias

    // Lista de registros de estudio
    private val _registros = mutableStateListOf<RegistroEstudio>()
    val registros: List<RegistroEstudio> = _registros

    private var timerJob: Job? = null
    private var tiempoRestante = mutableStateOf(0L)

    init {
        viewModelScope.launch {
            cargarMaterias()
            cargarRegistros()
        }
    }

    // --- Lógica de Materias ---
    private suspend fun cargarMaterias() {
        val loadedMaterias = loadData<List<Materia>>(context, MATERIAS_FILENAME, emptyList())
        _materias.clear()
        _materias.addAll(loadedMaterias)
    }

    private fun guardarMaterias() {
        viewModelScope.launch {
            saveData(context, MATERIAS_FILENAME, _materias.toList())
        }
    }

    fun añadirMateria(nombre: String) {
        if (_materias.any { it.nombre == nombre }) return
        // Asignar un color por defecto (ej. un color fijo o uno aleatorio)
        _materias.add(Materia(id = UUID.randomUUID().toString(), nombre = nombre, color = 0xFF42A5F5)) // Color azul por defecto
        guardarMaterias()
    }

    fun eliminarMateria(materiaId: String) {
        _materias.removeIf { it.id == materiaId }
        guardarMaterias()
    }

    // --- Lógica de Registros de Estudio ---
    private suspend fun cargarRegistros() {
        val loadedRegistros = loadData<List<RegistroEstudio>>(context, REGISTROS_FILENAME, emptyList())
        _registros.clear()
        _registros.addAll(loadedRegistros)
    }

    private fun guardarRegistros() {
        viewModelScope.launch {
            saveData(context, REGISTROS_FILENAME, _registros.toList())
        }
    }

    private fun añadirRegistro(materiaId: String) {
        val nuevaSesion = RegistroEstudio(
            fecha = System.currentTimeMillis(),
            materiaId = materiaId,
            duracionMinutos = tiempoEstudio.value,
            notas = ""
        )
        _registros.add(nuevaSesion)
        guardarRegistros()
    }

    fun eliminarRegistro(registro: RegistroEstudio) {
        _registros.remove(registro)
        guardarRegistros()
    }

    fun obtenerSesionesPorFecha(fecha: Date): List<RegistroEstudio> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaString = dateFormat.format(fecha)
        return _registros.filter { dateFormat.format(Date(it.fecha)) == fechaString }
    }

    fun obtenerMateriaPorId(materiaId: String): Materia? {
        return _materias.find { it.id == materiaId }
    }

    // --- Lógica del Temporizador ---
    fun iniciarTemporizador(materia: Materia?, onFinish: () -> Unit) {
        if (!isTimerRunning.value) {
            isTimerRunning.value = true
            tiempoRestante.value = (tiempoEstudio.value * 60 * 1000).toLong()

            timerJob = viewModelScope.launch {
                while (tiempoRestante.value > 0) {
                    delay(1000)
                    tiempoRestante.value -= 1000
                    actualizarUI(tiempoRestante.value)
                }

                if (materia != null) {
                    añadirRegistro(materia.id)
                }
                isTimerRunning.value = false
                onFinish()
            }
        }
    }

    fun pausarTemporizador() {
        timerJob?.cancel()
        isTimerRunning.value = false
    }

    fun reiniciarTemporizador() {
        timerJob?.cancel()
        isTimerRunning.value = false
        tiempoRestante.value = (tiempoEstudio.value * 60 * 1000).toLong()
        actualizarUI(tiempoRestante.value)
    }

    private fun actualizarUI(milisegundos: Long) {
        val minutos = (milisegundos / 1000) / 60
        val segundos = (milisegundos / 1000) % 60
        estadoTemporizador.value = String.format("%02d:%02d", minutos, segundos)
    }
}