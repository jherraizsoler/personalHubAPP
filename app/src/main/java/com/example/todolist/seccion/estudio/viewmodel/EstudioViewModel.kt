
package com.example.todolist.seccion.estudio.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.seccion.estudio.data.Materia
import com.example.todolist.seccion.estudio.data.RegistroEstudio
import com.example.todolist.seccion.estudio.data.repository.EstudioRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class EstudioViewModel(private val context: Context) : ViewModel() {

    private val repository = EstudioRepository(context)

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
    private var horaInicioSesion: Long = 0L

    init {
        viewModelScope.launch {
            cargarDatos()
        }
    }

    // --- Lógica de Carga Inicial ---
    private fun cargarDatos() {
        viewModelScope.launch {
            val loadedMaterias = repository.loadMaterias()
            _materias.clear()
            _materias.addAll(loadedMaterias)

            val loadedRegistros = repository.loadRegistros()
            _registros.clear()
            _registros.addAll(loadedRegistros)
        }
    }

    // --- Lógica de Materias ---
    fun agregarMateria(nombre: String, color: Long) {
        viewModelScope.launch {
            val nuevaMateria = Materia(nombre = nombre, color = color)
            _materias.add(nuevaMateria)
            repository.saveMaterias(_materias.toList())
        }
    }

    fun eliminarMateria(materia: Materia) {
        viewModelScope.launch {
            _materias.remove(materia)
            repository.saveMaterias(_materias.toList())

            if (materiaSeleccionada.value == materia) {
                materiaSeleccionada.value = null
            }
        }
    }

    fun obtenerMateriaPorId(materiaId: String): Materia? {
        return _materias.find { it.id == materiaId }
    }

    // --- Lógica de Registros de Estudio ---
    private fun añadirRegistro(materiaId: String, horaInicio: Long, horaFin: Long) {
        viewModelScope.launch {
            val nuevaSesion = RegistroEstudio(
                materiaId = materiaId,
                duracionMinutos = tiempoEstudio.value,
                notas = "",
                horaInicio = horaInicio,
                horaFin = horaFin
            )
            _registros.add(nuevaSesion)
            repository.saveRegistros(_registros.toList())
        }
    }

    fun eliminarRegistro(registro: RegistroEstudio) {
        viewModelScope.launch {
            _registros.remove(registro)
            repository.saveRegistros(_registros.toList())
        }
    }

    fun obtenerSesionesPorFecha(fecha: Date): List<RegistroEstudio> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaString = dateFormat.format(fecha)
        return _registros.filter { dateFormat.format(Date(it.horaInicio)) == fechaString }
    }

    // --- Lógica del Temporizador ---
    fun iniciarTemporizador(materia: Materia?, onFinish: () -> Unit) {
        if (!isTimerRunning.value) {
            if (materia == null) {
                return
            }
            horaInicioSesion = System.currentTimeMillis()
            isTimerRunning.value = true
            tiempoRestante.value = (tiempoEstudio.value * 60 * 1000).toLong()

            timerJob = viewModelScope.launch {
                while (tiempoRestante.value > 0) {
                    delay(1000)
                    tiempoRestante.value -= 1000
                    actualizarUI(tiempoRestante.value)
                }

                val horaFinSesion = System.currentTimeMillis()
                añadirRegistro(materia.id, horaInicioSesion, horaFinSesion)
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