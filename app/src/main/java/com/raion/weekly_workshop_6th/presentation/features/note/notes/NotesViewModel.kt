package com.raion.weekly_workshop_6th.presentation.features.note.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raion.weekly_workshop_6th.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> = _state.asStateFlow()

    fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            noteRepository.getNotes().fold(
                onSuccess = { notes ->
                    _state.update { it.copy(isLoading = false, notes = notes) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Gagal memuat catatan"
                        )
                    }
                }
            )
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            noteRepository.deleteNote(noteId)
                .fold(
                    onSuccess = {
                        loadNotes()
                    },
                    onFailure = { error ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Gagal menghapus catatan"
                            )
                        }
                    }
                )
        }
    }
}
