package com.example.todolist.seccion.productividad.data.repository

import android.content.Context
import com.example.todolist.data.loadEncryptedData
import com.example.todolist.data.saveEncryptedData
import com.example.todolist.seccion.productividad.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoRepository(private val context: Context, private val coroutineScope: CoroutineScope) {

    private val FILENAME = "tasks.json"

    private val _todos = MutableStateFlow(emptyList<Task>())
    val todos: StateFlow<List<Task>> = _todos.asStateFlow()

    init {
        // Carga las tareas al inicializar el repositorio usando el CoroutineScope inyectado
        coroutineScope.launch(Dispatchers.IO) {
            loadTodos()
        }
    }

    private suspend fun loadTodos() {
        val loadedTodos = loadEncryptedData(context, FILENAME, emptyList<Task>())
        _todos.value = loadedTodos
    }

    private suspend fun saveTodos() {
        saveEncryptedData(context, FILENAME, _todos.value)
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
