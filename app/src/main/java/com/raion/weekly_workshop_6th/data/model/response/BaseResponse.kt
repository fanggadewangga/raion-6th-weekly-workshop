package com.raion.weekly_workshop_6th.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val data: T,
    val message: String,
    val status: String
)