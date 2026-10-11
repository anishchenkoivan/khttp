package ru.notes.controller

import ru.notes.controller.dto.NoteDto
import ru.notes.controller.dto.NotesCollectionDto
import ru.notes.model.Note
import ru.notes.model.NotesCollection

fun Note.toDto() = NoteDto(
    id = id,
    title = title,
    content = content,
)

fun NotesCollection.toDto() = NotesCollectionDto(
    id = id,
    title = title,
    notes = notes.map { it.toDto() }
)
