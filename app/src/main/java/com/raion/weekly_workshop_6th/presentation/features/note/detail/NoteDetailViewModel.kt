package com.raion.weekly_workshop_6th.presentation.features.note.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.net.toUri

class NoteDetailViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailState())
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _state.update { it.copy(title = newTitle) }
    }

    fun onContentChange(newContent: String) {
        _state.update { it.copy(content = newContent) }
    }

    fun setImageUri(uri: Uri) {
        _state.update { it.copy(imageUri = uri) }
    }

    fun resetForm() {
        _state.value = NoteDetailState()
    }

    fun loadNote(noteId: String) {
        viewModelScope.launch {

        }
    }

    fun createNote(file: File?) {
        viewModelScope.launch {

        }
    }

    fun updateNote(noteId: String, file: File?) {
        viewModelScope.launch {

        }
    }


    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Can't open input stream from URI")
        val tempFile = File.createTempFile("upload_", ".tmp", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return tempFile
    }
}