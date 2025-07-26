package com.raion.weekly_workshop_6th.domain.model

import kotlinx.serialization.SerialName

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val createdAt: String,
    val updatedAt: String,
    val userId: String
)