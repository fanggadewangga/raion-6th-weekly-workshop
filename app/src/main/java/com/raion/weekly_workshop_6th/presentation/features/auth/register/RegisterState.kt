package com.raion.weekly_workshop_6th.presentation.features.auth.register

data class RegisterState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)