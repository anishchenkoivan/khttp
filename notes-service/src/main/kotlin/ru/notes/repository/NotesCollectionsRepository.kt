package ru.notes.repository

import ru.notes.model.NotesCollection
import java.util.UUID

interface NotesCollectionsRepository {
    fun findById(id: UUID): NotesCollection?
    fun save(collection: NotesCollection): NotesCollection
}
