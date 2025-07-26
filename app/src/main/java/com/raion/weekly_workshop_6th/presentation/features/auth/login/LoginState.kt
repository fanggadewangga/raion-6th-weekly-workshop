package com.raion.weekly_workshop_6th.presentation.features.auth.login

data class LoginState (
    val email: String = "",
    val password: String = "",
    val token: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)