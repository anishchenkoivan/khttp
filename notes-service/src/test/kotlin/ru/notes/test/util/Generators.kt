package ru.notes.test.util

import ru.notes.model.Note
import ru.notes.model.NotesCollection
import java.util.UUID

fun generateNote(
    id: UUID = UUID.randomUUID(),
    title: String = "Note title",
    content: String = "Note content",
) = Note(
    id = id,
    title = title,
    content = content,
)

fun generateNotesCollection(
    id: UUID = UUID.randomUUID(),
    title: String = "Note collection title",
    notes: List<Note> = listOf(),
) = NotesCollection(
    id = id,
    title = title,
    notes = notes,
)