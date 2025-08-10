package com.example.todolist.authentication

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val password: String)

data class AuthResponse(val token: String?, val msg: String?)

interface AuthService {
    @POST("api/auth/register")
    suspend fun registerUser(@Body user: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun loginUser(@Body user: LoginRequest): Response<AuthResponse>
}
