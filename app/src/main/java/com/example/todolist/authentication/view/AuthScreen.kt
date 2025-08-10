package com.example.todolist.authentication.view

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.authentication.viewmodel.AuthUiState
import com.example.todolist.authentication.viewmodel.AuthViewModel

/**
 * Función auxiliar para encontrar la FragmentActivity en la cadena de contexto.
 * Es necesaria para APIs como la de autenticación biométrica que requieren un FragmentActivity.
 */
private fun findFragmentActivity(context: Context): FragmentActivity? {
    if (context is FragmentActivity) {
        return context
    }
    if (context is ContextWrapper) {
        return findFragmentActivity(context.baseContext)
    }
    return null
}

/**
 * Pantalla de autenticación principal.
 *
 * @param onAuthSuccess Lambda que se ejecuta cuando la autenticación es exitosa.
 * @param authViewModel El ViewModel que maneja la lógica de autenticación.
 */
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    authViewModel: AuthViewModel = createAuthViewModel()
) {
    // Recolecta el estado de la UI del ViewModel
    val uiState by authViewModel.uiState.collectAsState()

    // Obtiene el contexto actual de forma segura para encontrar la FragmentActivity.
    // Usamos el helper findFragmentActivity para evitar la excepción ClassCastException.
    val context = LocalContext.current
    val fragmentActivity = findFragmentActivity(context)

    // Lógica para navegar a la pantalla principal si el usuario ya está autenticado
    if (uiState.isLoggedIn) {
        onAuthSuccess()
    }

    // Estado local para alternar entre login y registro
    var isRegisterMode by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isRegisterMode) "Registro" else "Iniciar Sesión",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Formulario de entrada
            AuthForm(
                uiState = uiState,
                onUsernameChange = authViewModel::onUsernameChange,
                onPasswordChange = authViewModel::onPasswordChange,
                isRegisterMode = isRegisterMode,
                onLoginClick = authViewModel::login,
                onRegisterClick = authViewModel::register
            )

            // Indicador de carga y mensajes de error/éxito
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            } else if (!uiState.error.isNullOrEmpty()) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            } else if (!uiState.message.isNullOrEmpty()) {
                Text(text = uiState.message ?: "", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para alternar entre iniciar sesión y registrarse
            TextButton(
                onClick = { isRegisterMode = !isRegisterMode },
                enabled = !uiState.isLoading
            ) {
                Text(text = if (isRegisterMode) "¿Ya tienes una cuenta? Inicia sesión" else "¿No tienes una cuenta? Regístrate")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de autenticación biométrica (solo si el dispositivo es compatible)
            if (authViewModel.hasBiometricCapability()) {
                // Pasamos el FragmentActivity encontrado, si existe.
                BiometricAuthButton(
                    onAuthSuccess = onAuthSuccess,
                    authViewModel = authViewModel,
                    fragmentActivity = fragmentActivity,
                    isLoading = uiState.isLoading
                )
            }
        }
    }
}

/**
 * Función auxiliar para crear el ViewModel. Esto ayuda a forzar la resolución de la referencia de la factoría.
 */
@Composable
fun createAuthViewModel(): AuthViewModel {
    val application = LocalContext.current.applicationContext as Application
    return viewModel(factory = AuthViewModel.Companion.Factory(application))
}

@Composable
fun AuthForm(
    uiState: AuthUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isRegisterMode: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    OutlinedTextField(
        value = uiState.username,
        onValueChange = onUsernameChange,
        label = { Text("Nombre de usuario") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        enabled = !uiState.isLoading
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = uiState.password,
        onValueChange = onPasswordChange,
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = !uiState.isLoading
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = if (isRegisterMode) onRegisterClick else onLoginClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !uiState.isLoading && uiState.username.isNotEmpty() && uiState.password.isNotEmpty()
    ) {
        Text(text = if (isRegisterMode) "Registrarme" else "Iniciar Sesión")
    }
}

@Composable
fun BiometricAuthButton(
    onAuthSuccess: () -> Unit,
    authViewModel: AuthViewModel,
    fragmentActivity: FragmentActivity?,
    isLoading: Boolean
) {
    ElevatedButton(
        // Deshabilita el botón si no se puede encontrar la FragmentActivity.
        onClick = { if (fragmentActivity != null) authViewModel.authenticateWithBiometrics(fragmentActivity, onAuthSuccess) },
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading && fragmentActivity != null
    ) {
        // Usa un icono para la huella dactilar, si lo tienes disponible
        // Icon(painter = painterResource(id = R.drawable.ic_fingerprint), contentDescription = "Huella dactilar")
        Text(text = "Iniciar Sesión con Huella Dactilar")
    }
}



