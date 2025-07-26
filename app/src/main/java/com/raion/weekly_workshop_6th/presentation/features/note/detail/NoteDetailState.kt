package com.raion.weekly_workshop_6th.presentation.features.note.detail

import android.net.Uri

data class NoteDetailState(
    val title: String = "",
    val content: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)