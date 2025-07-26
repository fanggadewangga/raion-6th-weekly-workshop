package com.raion.weekly_workshop_6th.data.repository

import android.util.Log
import com.raion.weekly_workshop_6th.data.model.request.LoginRequest
import com.raion.weekly_workshop_6th.data.model.request.RegisterRequest
import com.raion.weekly_workshop_6th.data.model.response.ErrorResponse
import com.raion.weekly_workshop_6th.data.model.response.LoginResponse
import com.raion.weekly_workshop_6th.data.model.response.RegisterResponse
import com.raion.weekly_workshop_6th.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {

    // Handles the login request
    override suspend fun login(email: String, password: String): Result<String> = try {
        // Send a POST request to /api/auth/login with JSON body
        val response = client.post("api/auth/login") {
            contentType(ContentType.Application.Json) // Set content type to application/json
            setBody(LoginRequest(email, password))    // Send email and password as request body
        }

        // If response status is 200 OK, extract token from body
        if (response.status == HttpStatusCode.OK) {
            val body = response.body<LoginResponse>()
            Log.d("AuthRepository", "Login success: $body")
            Result.success(body.data.token) // Return token inside Result.success
        } else {
            // If login failed (e.g. wrong credentials), extract error message from response
            val error = response.body<ErrorResponse>()
            Log.d("AuthRepository", "Login failed: $error")
            Result.failure(Exception(error.error))
        }
    } catch (e: ClientRequestException) {
        // Catch specific client error (e.g. 4xx) and return the parsed error message
        val error = e.response.body<ErrorResponse>()
        Result.failure(Exception(error.error))
    } catch (e: Exception) {
        // Catch any other exception (e.g. network failure)
        Log.e("AuthRepository", "Login failed", e)
        Result.failure(e)
    }

    // Handles the registration request
    override suspend fun register(email: String, name: String, password: String): Result<Unit> = try {
        // Send a POST request to /api/auth/register with JSON body
        val response = client.post("api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(email, name, password))
        }

        // If registration is successful (201 Created), return success
        if (response.status == HttpStatusCode.Created) {
            val body = response.body<RegisterResponse>()
            Log.d("AuthRepository", "Register success: $body")
            Result.success(Unit)
        } else {
            // If registration failed (e.g. email already exists), extract error
            val error = response.body<ErrorResponse>()
            Log.d("AuthRepository", "Register failed: ${error.error}")
            Result.failure(Exception(error.error))
        }
    } catch (e: Exception) {
        Log.e("AuthRepository", "Register failed", e)
        Result.failure(e)
    }
}