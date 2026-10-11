package ru.notes.repository.memory

import ru.notes.model.NotesCollection
import ru.notes.repository.NotesCollectionsRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryNotesCollectionsRepository: NotesCollectionsRepository {
    private val collections = ConcurrentHashMap<UUID, NotesCollection>()

    override fun findById(id: UUID): NotesCollection? = collections[id]

    override fun save(collection: NotesCollection): NotesCollection {
        collections[collection.id] = collection
        return collection
    }

    override fun delete(id: UUID) {
        collections.remove(id)
    }
}
