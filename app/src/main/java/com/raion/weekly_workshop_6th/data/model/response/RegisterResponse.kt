package com.raion.weekly_workshop_6th.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val data: RegisterData,
    val message: String,
    val status: String
)

@Serializable
data class RegisterData(
    val message: String
)