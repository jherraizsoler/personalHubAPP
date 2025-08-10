package com.example.todolist.authentication.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.authentication.AuthResponse
import com.example.todolist.authentication.BiometricAuthManager
import com.example.todolist.authentication.LoginRequest
import com.example.todolist.authentication.RegisterRequest
import com.example.todolist.authentication.RetrofitClient
import com.example.todolist.authentication.TokenManager
import com.example.todolist.authentication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(RetrofitClient.authService)
    private val tokenManager = TokenManager(application)
    private val biometricAuthManager = BiometricAuthManager(application)

    // Estado de la UI
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        // Verificar si ya hay un token al iniciar el ViewModel
        val token = tokenManager.getToken()
        if (token != null) {
            _uiState.value = _uiState.value.copy(
                isLoggedIn = true,
                token = token
            )
        }
    }

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun register() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val request = RegisterRequest(
                username = _uiState.value.username,
                password = _uiState.value.password
            )
            val response = repository.register(request)
            if (response?.token != null) {
                // Registro exitoso, guardamos el token
                tokenManager.saveToken(response.token)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    isRegistered = true,
                    token = response.token,
                    message = response.msg
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = response?.msg ?: "Error de conexión"
                )
            }
        }
    }

    fun login() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val request = LoginRequest(
                username = _uiState.value.username,
                password = _uiState.value.password
            )
            val response = repository.login(request)
            if (response?.token != null) {
                // Login exitoso, guardamos el token
                tokenManager.saveToken(response.token)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    token = response.token
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = response?.msg ?: "Error de conexión"
                )
            }
        }
    }

    fun hasBiometricCapability(): Boolean {
        return biometricAuthManager.canAuthenticate()
    }

    fun authenticateWithBiometrics(activity: FragmentActivity) {
        biometricAuthManager.showBiometricPrompt(
            activity,
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = true
                )
            },
            onError = { errorCode, errString ->
                _uiState.value = _uiState.value.copy(
                    error = "Error de autenticación biométrica: $errString"
                )
            }
        )
    }

    fun logout() {
        tokenManager.clearToken()
        _uiState.value = AuthUiState() // Reinicia el estado del ViewModel
    }
}

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val token: String? = null,
    val message: String? = null,
    val error: String? = null
)