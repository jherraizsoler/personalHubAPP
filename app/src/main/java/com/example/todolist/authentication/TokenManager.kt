package com.example.todolist.authentication

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class TokenManager(private val context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            "secure_auth_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("jwt_token", token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun clearToken() {
        with(sharedPreferences.edit()) {
            remove("jwt_token")
            apply()
        }
    }
}