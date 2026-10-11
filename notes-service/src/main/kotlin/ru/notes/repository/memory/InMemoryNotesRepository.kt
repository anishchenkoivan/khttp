package ru.notes.repository.memory

import ru.notes.model.Note
import ru.notes.repository.NotesRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryNotesRepository : NotesRepository {
    private val notes = ConcurrentHashMap<UUID, Note>()

    override fun findById(id: UUID): Note? = notes[id]

    override fun save(note: Note): Note {
        notes[note.id] = note
        return note
    }
}
