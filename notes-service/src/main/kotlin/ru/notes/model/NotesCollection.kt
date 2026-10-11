package ru.notes.model

import java.util.UUID

data class NotesCollection(
    val id: UUID,
    val title: String,
    val notes: List<Note>
)
