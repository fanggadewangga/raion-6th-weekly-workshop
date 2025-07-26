package com.raion.weekly_workshop_6th.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(email: String, name: String,  password: String): Result<Unit>
}