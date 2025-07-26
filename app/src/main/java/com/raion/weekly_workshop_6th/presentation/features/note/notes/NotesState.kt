package com.raion.weekly_workshop_6th.presentation.features.note.notes

import com.raion.weekly_workshop_6th.domain.model.Note

data class NotesState(
    val isLoading: Boolean = false,
    val notes: List<Note> = emptyList(),
    val error: String? = null
)