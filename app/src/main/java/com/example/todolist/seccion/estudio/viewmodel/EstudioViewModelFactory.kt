package com.example.todolist.seccion.estudio.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EstudioViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstudioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstudioViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

