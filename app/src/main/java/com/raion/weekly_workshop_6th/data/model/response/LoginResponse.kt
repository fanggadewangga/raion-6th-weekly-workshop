package com.raion.weekly_workshop_6th.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val data: LoginData,
    val message: String,
    val status: String
)

@Serializable
data class LoginData(
    val token: String,
    val user: User
)

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)
