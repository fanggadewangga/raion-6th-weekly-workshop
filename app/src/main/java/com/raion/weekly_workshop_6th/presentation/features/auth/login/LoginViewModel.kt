package com.raion.weekly_workshop_6th.presentation.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raion.weekly_workshop_6th.data.local.TokenStorage
import com.raion.weekly_workshop_6th.domain.repository.AuthRepository
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
            _state.update { it.copy(isLoading = true) }

            val result = authRepository.login(_state.value.email, _state.value.password)

            result.fold(
                onSuccess = { token ->
                    tokenStorage.saveToken(token)
                    _state.update { it.copy(isLoading = false, token = token, error = null) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Login gagal") }
                }
            )
        }
    }
}