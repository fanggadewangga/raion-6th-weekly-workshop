package com.raion.weekly_workshop_6th.presentation.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository, private val tokenStorage: TokenStorage) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _state.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _state.update { it.copy(password = newPassword) }
    }

    fun login() {
        viewModelScope.launch {

        }
    }
}