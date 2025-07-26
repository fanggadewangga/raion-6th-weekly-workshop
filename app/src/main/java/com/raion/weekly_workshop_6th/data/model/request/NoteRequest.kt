package com.raion.weekly_workshop_6th.data.model.request

import java.io.File

data class NoteRequest(
    val title: String,
    val content: String,
    val imageFile: File?
)