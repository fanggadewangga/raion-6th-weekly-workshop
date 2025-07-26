package com.raion.weekly_workshop_6th.data.local

import android.content.Context
import androidx.core.content.edit

class TokenStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) { sharedPreferences.edit { putString("jwt_token", token) } }

    fun getToken(): String? = sharedPreferences.getString("jwt_token", null)
}