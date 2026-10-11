package ru.notes.repository

import ru.notes.model.Note
import java.util.UUID

interface NotesRepository {
    fun findById(id: UUID): Note?
    fun save(note: Note): Note
}
