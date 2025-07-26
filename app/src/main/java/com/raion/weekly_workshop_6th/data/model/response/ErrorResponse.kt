package com.raion.weekly_workshop_6th.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String,
    val status: String
)