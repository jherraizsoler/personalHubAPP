package com.example.todolist.seccion.productividad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.seccion.productividad.data.repository.TodoRepository

class ProductivityViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}