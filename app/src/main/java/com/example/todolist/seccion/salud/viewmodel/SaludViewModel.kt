package com.example.todolist.seccion.salud.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.loadData
import com.example.todolist.data.saveData
import com.example.todolist.seccion.salud.data.Pastilla
import com.example.todolist.seccion.salud.data.RegistroAlimentacion
import com.example.todolist.seccion.salud.data.SaludState
import kotlinx.coroutines.launch

class SaludViewModel(private val context: Context) : ViewModel() {

    private val FILENAME = "salud.json"

    private val _pastillas = mutableStateListOf<Pastilla>()
    val pastillas: List<Pastilla> = _pastillas

    private val _registrosAlimentacion = mutableStateListOf<RegistroAlimentacion>()
    val registrosAlimentacion: List<RegistroAlimentacion> = _registrosAlimentacion

    init {
        cargarEstado()
    }

    private fun cargarEstado() {
        viewModelScope.launch {
            val loadedState = loadData(context, FILENAME, SaludState(emptyList(), emptyList()))
            _pastillas.clear()
            _pastillas.addAll(loadedState.pastillas)
            _registrosAlimentacion.clear()
            _registrosAlimentacion.addAll(loadedState.registrosAlimentacion)
        }
    }


    private fun guardarEstado() {
        viewModelScope.launch {
            val currentState = SaludState(_pastillas, _registrosAlimentacion)
            saveData(context, FILENAME, currentState)
        }
    }

    fun añadirPastilla(nombre: String, dosis: String, frecuencia: String) {
        val nuevaPastilla = Pastilla(
            nombre = nombre,
            dosis = dosis,
            frecuencia = frecuencia
        )
        _pastillas.add(nuevaPastilla)
        guardarEstado()
    }

    fun eliminarPastilla(pastilla: Pastilla) {
        _pastillas.remove(pastilla)
        guardarEstado()
    }

    fun añadirRegistroAlimentacion(comida: String, fecha: Long, calorias: Int, notas: String) {
        val nuevoRegistro = RegistroAlimentacion(
            comida = comida,
            fecha = fecha,
            calorias = calorias,
            notas = notas
        )
        _registrosAlimentacion.add(nuevoRegistro)
        guardarEstado()
    }

    fun eliminarRegistroAlimentacion(registro: RegistroAlimentacion) {
        _registrosAlimentacion.remove(registro)
        guardarEstado()
    }
}