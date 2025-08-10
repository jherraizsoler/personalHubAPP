package com.example.todolist.authentication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.fragment.app.FragmentActivity
import com.example.todolist.authentication.LoginRequest
import com.example.todolist.authentication.RegisterRequest
import com.example.todolist.authentication.RetrofitClient
import com.example.todolist.authentication.TokenManager
import com.example.todolist.authentication.biometric.BiometricAuthManager
import com.example.todolist.authentication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Define el estado de la UI para el login, registro, etc.
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

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(RetrofitClient.authService)
    private val tokenManager = TokenManager(application)
    private val biometricAuthManager = BiometricAuthManager(application)

    // Estado de la UI
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        // Al iniciar, el ViewModel verifica si ya hay un token guardado.
        // Si lo encuentra, el usuario se considera logueado.
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

    /**
     * Esta función ha sido corregida para aceptar FragmentActivity en lugar de MainActivity.
     * Esto resuelve el error de incompatibilidad de tipos.
     */
    fun authenticateWithBiometrics(activity: FragmentActivity, onAuthSuccess: () -> Unit) {
        biometricAuthManager.showBiometricPrompt(
            activity,
            onSuccess = { // Corregido: la función onSuccess ahora acepta un parámetro
                // Si la autenticación biométrica es exitosa, se considera que el usuario ha iniciado sesión.
                // Podrías cargar el token aquí si es necesario, pero para esta lógica
                // es suficiente con actualizar el estado.
                _uiState.value = _uiState.value.copy(isLoggedIn = true)
                onAuthSuccess()
            },
            onError = { errorCode, errString ->
                _uiState.value = _uiState.value.copy(
                    error = "Error de autenticación biométrica: $errString"
                )
            }
        )
    }

    /**
     * Esta función es la clave para tu caso de uso.
     * Al llamar a `logout()`, se elimina el token guardado y se reinicia el estado
     * del ViewModel, lo que hará que la aplicación inicie sin sesión.
     */
    fun logout() {
        tokenManager.clearToken()
        _uiState.value = AuthUiState() // Reinicia el estado del ViewModel
    }

    companion object {
        class Factory(private val application: Application) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return AuthViewModel(application) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
