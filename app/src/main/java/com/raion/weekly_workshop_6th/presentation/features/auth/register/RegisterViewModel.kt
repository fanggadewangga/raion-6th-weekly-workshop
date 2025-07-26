package com.raion.weekly_workshop_6th.presentation.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onNameChange(newName: String) {
        _state.update { it.copy(name = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _state.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _state.update { it.copy(password = newPassword) }
    }

    fun register() {
        viewModelScope.launch {

        }
    }
}