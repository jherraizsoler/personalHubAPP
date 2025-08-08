package com.example.todolist.seccion.salud.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SaludViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaludViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SaludViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}