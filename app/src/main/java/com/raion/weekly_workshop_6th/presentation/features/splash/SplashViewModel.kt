package com.raion.weekly_workshop_6th.presentation.features.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val tokenStorage: TokenStorage) : ViewModel() {
    private val _hasToken = MutableStateFlow<Boolean?>(null)
    val hasToken: StateFlow<Boolean?> = _hasToken.asStateFlow()

    init {
        viewModelScope.launch {
            val token = tokenStorage.getToken()
            Log.d("Token", token.toString())
            _hasToken.value = !token.isNullOrEmpty()
        }
    }
}