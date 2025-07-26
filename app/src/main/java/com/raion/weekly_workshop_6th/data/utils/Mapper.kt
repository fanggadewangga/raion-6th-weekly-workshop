package com.raion.weekly_workshop_6th.data.utils

import com.raion.weekly_workshop_6th.data.model.response.NoteResponse
import com.raion.weekly_workshop_6th.domain.model.Note

fun NoteResponse.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userId = userId
    )
}

fun List<NoteResponse>.toDomainList(): List<Note> = map { it.toDomain() }