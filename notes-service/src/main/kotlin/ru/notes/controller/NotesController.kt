package ru.notes.controller

import org.khttp.http.request.HttpRequest
import org.khttp.http.response.HttpResponseDetails
import ru.notes.repository.NotesCollectionsRepository
import ru.notes.repository.NotesRepository
import java.io.OutputStream

class NotesController(
    val notesRepository: NotesRepository,
    val collectionsRepository: NotesCollectionsRepository,
) {
    fun findNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        TODO()
    }

    fun createNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        TODO()
    }

    fun deleteNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        TODO()
    }

    fun updateNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        TODO()
    }

    fun findCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        TODO()
    }

    fun createCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        TODO()
    }

    fun deleteCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        TODO()
    }

    fun updateCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        TODO()
    }
}
