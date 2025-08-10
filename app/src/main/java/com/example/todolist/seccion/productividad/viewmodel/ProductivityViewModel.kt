package com.example.todolist.seccion.productividad.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todolist.seccion.productividad.data.Task
import com.example.todolist.seccion.productividad.data.TaskType
import com.example.todolist.seccion.productividad.data.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

class ProductivityViewModel(private val context: Context) : ViewModel() {

    // Creamos la instancia del repositorio aquí, usando el viewModelScope
    private val repository = TodoRepository(context, viewModelScope)

    val items: StateFlow<List<Task>> = repository.todos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val allCategories: StateFlow<List<String>> = items
        .map { todos ->
            todos.flatMap { it.categories }.distinct().sorted()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addItem(name: String, categories: List<String>, type: TaskType, content: String = "") {
        viewModelScope.launch {
            val newTask = Task(name = name, categories = categories, type = type, content = content)
            repository.addItem(newTask)
        }
    }

    fun updateItem(task: Task) {
        viewModelScope.launch {
            repository.updateItem(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleItemCompletion(task: Task) {
        viewModelScope.launch {
            val isCompletedNow = !task.isCompleted
            val updatedTask = task.copy(
                isCompleted = isCompletedNow,
                completedAt = if (isCompletedNow) Instant.now().toString() else null
            )
            repository.updateItem(updatedTask)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun softDeleteItem(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                isDeleted = true,
                deletedAt = Instant.now().toString()
            )
            repository.updateItem(updatedTask)
        }
    }

    fun restoreItem(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                isDeleted = false,
                deletedAt = null
            )
            repository.updateItem(updatedTask)
        }
    }

    fun permanentlyDeleteItem(task: Task) {
        viewModelScope.launch {
            repository.permanentlyDeleteTodo(task)
        }
    }

    fun deleteCategory(category: String) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProductivityViewModel(context) as T
                }
            }
    }
}