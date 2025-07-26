package com.raion.weekly_workshop_6th.presentation.features.note.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raion.weekly_workshop_6th.domain.repository.NoteRepository
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
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            noteRepository.getNoteById(noteId).fold(
                onSuccess = { note ->
                    _state.update {
                        it.copy(
                            title = note.title,
                            content = note.content,
                            imageUri = note.imageUrl?.toUri(),
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            errorMessage = error.message ?: "Gagal memuat data",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    fun createNote(file: File?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            val title = _state.value.title
            val content = _state.value.content

            noteRepository.createNote(title, content, file).fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Catatan berhasil dibuat"
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal membuat catatan"
                        )
                    }
                }
            )
        }
    }

    fun updateNote(noteId: String, file: File?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            val title = _state.value.title
            val content = _state.value.content

            noteRepository.updateNote(noteId, title, content, file).fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Catatan berhasil diperbarui"
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal memperbarui catatan"
                        )
                    }
                }
            )
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