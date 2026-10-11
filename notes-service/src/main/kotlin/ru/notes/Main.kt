package ru.notes

import org.khttp.server.KhttpServer
import ru.notes.controller.NotesController
import ru.notes.controller.route
import ru.notes.repository.memory.InMemoryNotesCollectionsRepository
import ru.notes.repository.memory.InMemoryNotesRepository

fun main() {
    val server = KhttpServer()

    val controller = NotesController(
        notesRepository = InMemoryNotesRepository(),
        collectionsRepository = InMemoryNotesCollectionsRepository(),
    )
    route(controller, server.router)

    server.listen(8081)
}
