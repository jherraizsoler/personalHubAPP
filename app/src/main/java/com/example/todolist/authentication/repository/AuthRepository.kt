package com.example.todolist.authentication.repository

import com.example.todolist.authentication.AuthService
import com.example.todolist.authentication.LoginRequest
import com.example.todolist.authentication.RegisterRequest
import com.example.todolist.authentication.AuthResponse

class AuthRepository(private val authService: AuthService) {
    suspend fun register(request: RegisterRequest): AuthResponse? {
        val response = authService.registerUser(request)
        return if (response.isSuccessful) {
            response.body()
        } else {
            AuthResponse(null, "Error al registrar: ${response.code()}")
        }
    }

    suspend fun login(request: LoginRequest): AuthResponse? {
        val response = authService.loginUser(request)
        return if (response.isSuccessful) {
            response.body()
        } else {
            AuthResponse(null, "Error al iniciar sesión: ${response.code()}")
        }
    }
}