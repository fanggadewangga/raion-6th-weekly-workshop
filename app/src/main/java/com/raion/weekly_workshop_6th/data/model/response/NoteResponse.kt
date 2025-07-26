package com.raion.weekly_workshop_6th.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse(
    val id: String,
    val title: String,
    val content: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("user_id") val userId: String
)

@Serializable
data class NoteListData(
    val count: Int,
    val notes: List<NoteResponse>
)

@Serializable
data class NoteDetailData(
    val note: NoteResponse
)