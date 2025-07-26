package com.raion.weekly_workshop_6th.domain.repository

import android.net.Uri
import com.raion.weekly_workshop_6th.domain.model.Note
import java.io.File

interface NoteRepository {
    suspend fun getNotes(): Result<List<Note>>
    suspend fun getNoteById(id: String): Result<Note>
    suspend fun createNote(title: String, content: String?, image: File?): Result<Unit>
    suspend fun updateNote(id: String, title: String, content: String?, image: File?): Result<Unit>
    suspend fun deleteNote(id: String): Result<Unit>
}
