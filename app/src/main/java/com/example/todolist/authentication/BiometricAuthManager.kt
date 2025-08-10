package com.example.todolist.authentication.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Clase para manejar la autenticación biométrica (huella dactilar, rostro, etc.).
 *
 * Utiliza [BiometricManager] para verificar si el dispositivo puede autenticarse
 * biométricamente y [BiometricPrompt] para mostrar la interfaz de usuario.
 */
class BiometricAuthManager(private val context: Context) {

    // Executor para ejecutar los callbacks de la autenticación biométrica.
    // Es mejor usar un executor de un solo hilo para evitar problemas de concurrencia.
    private val executor: Executor = Executors.newSingleThreadExecutor()

    // El BiometricManager nos ayuda a verificar la capacidad biométrica del dispositivo.
    private val biometricManager = BiometricManager.from(context)

    /**
     * Verifica si el dispositivo es capaz de realizar una autenticación biométrica
     * fuerte (como huella dactilar o reconocimiento facial).
     *
     * @return `true` si el dispositivo puede autenticarse, `false` en caso contrario.
     */
    fun canAuthenticate(): Boolean {
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Muestra el diálogo de autenticación biométrica al usuario.
     *
     * @param activity La actividad que actuará como host para el diálogo.
     * @param onSuccess Callback que se ejecuta cuando la autenticación es exitosa.
     * Recibe el resultado de la autenticación.
     * @param onError Callback que se ejecuta cuando la autenticación falla o es cancelada.
     * Recibe un código de error y una descripción del error.
     */
    fun showBiometricPrompt(
        activity: FragmentActivity,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit
    ) {
        // Configura la información que se mostrará en el diálogo.
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio de sesión biométrico")
            .setSubtitle("Usa tu huella dactilar o rostro para iniciar sesión")
            .setNegativeButtonText("Cancelar")
            .build()

        // Crea una instancia de BiometricPrompt con el executor y un callback.
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                // Se llama cuando ocurre un error fatal durante la autenticación.
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString)
                }

                // Se llama cuando la autenticación es exitosa.
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }
                // No se necesita onAuthenticationFailed() en este caso, ya que onError
                // cubre la mayoría de los escenarios de fallo.
            })

        // Inicia el proceso de autenticación.
        biometricPrompt.authenticate(promptInfo)
    }
}

