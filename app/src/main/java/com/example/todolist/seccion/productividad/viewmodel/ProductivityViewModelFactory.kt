package com.example.todolist.seccion.productividad.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Fábrica para crear una instancia de [ProductivityViewModel] con las dependencias necesarias.
 *
 * Esta fábrica simplificada toma el Contexto de la aplicación y se lo pasa al ViewModel,
 * permitiendo que el ViewModel gestione internamente la creación del repositorio
 * y el ciclo de vida de las corrutinas de forma segura.
 */
class ProductivityViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Ahora la fábrica solo crea el ViewModel y le pasa el contexto.
            // El ViewModel se encargará de crear el repositorio.
            return ProductivityViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}