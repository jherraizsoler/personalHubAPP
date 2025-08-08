package com.example.todolist.seccion.productividad.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.todolist.data.loadData
import com.example.todolist.data.saveData
import com.example.todolist.seccion.productividad.data.Task
import com.example.todolist.seccion.productividad.data.TaskType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class TodoRepository(val context: Context) {

    private val FILENAME = "tasks.json"

    private val _todos = MutableStateFlow(emptyList<Task>())
    val todos: StateFlow<List<Task>> = _todos.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadTodos()
        }
    }

    private suspend fun loadTodos() {
        val loadedTodos = loadData<List<Task>>(context, FILENAME)
        loadedTodos?.let {
            _todos.value = it
        }
    }

    private suspend fun saveTodos() {
        saveData(context, FILENAME, _todos.value)
    }

    suspend fun addItem(newTask: Task) {
        _todos.value = _todos.value + newTask
        saveTodos()
    }

    // Esta función actualizará una tarea existente
    suspend fun updateItem(task: Task) {
        val updatedList = _todos.value.toMutableList()
        val index = updatedList.indexOfFirst { it.id == task.id }
        if (index != -1) {
            updatedList[index] = task
            _todos.value = updatedList
            saveTodos()
        }
    }

    // Esta función es opcional si updateTodo ya maneja el borrado suave
    // Pero es una buena práctica para tener una función específica
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun softDeleteTodo(task: Task) {
        val updatedTask = task.copy(
            isDeleted = true,
            deletedAt = Instant.now().toString()
        )
        updateItem(updatedTask)
    }

    // Esta función restaurará una tarea borrada
    suspend fun restoreTodo(task: Task) {
        val updatedTask = task.copy(
            isDeleted = false,
            deletedAt = null
        )
        updateItem(updatedTask)
    }

    // Esta función eliminará la tarea permanentemente
    suspend fun permanentlyDeleteTodo(task: Task) {
        val updatedList = _todos.value.toMutableList()
        updatedList.removeIf { it.id == task.id }
        _todos.value = updatedList
        saveTodos()
    }

    // AÑADE ESTA FUNCIÓN AL REPOSITORIO
    suspend fun deleteCategory(category: String) {
        _todos.value = _todos.value.map { task ->
            task.copy(categories = task.categories.filter { it != category })
        }
        saveTodos()
    }
}