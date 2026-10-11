package ru.notes.controller

import org.khttp.http.HttpStatus
import org.khttp.http.request.HttpRequest
import org.khttp.http.response.HttpResponseDetails
import ru.notes.controller.dto.Json
import ru.notes.controller.dto.NoteUpdateRequest
import ru.notes.controller.dto.NotesCollectionUpdateRequest
import ru.notes.model.Note
import ru.notes.model.NotesCollection
import ru.notes.repository.NotesCollectionsRepository
import ru.notes.repository.NotesRepository
import java.io.OutputStream
import java.util.UUID
import kotlin.jvm.java

class NotesController(
    val notesRepository: NotesRepository,
    val collectionsRepository: NotesCollectionsRepository,
) {
    private val mapper = Json.mapper

    fun findNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val note = notesRepository.findById(UUID.fromString(httpRequest.uri.queryParams["id"] ?: ""))
        return if (note == null) {
            HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        } else {
            mapper.writeValue(responseBodyStream, note.toDto())
            HttpResponseDetails(
                statusCode = HttpStatus.OK,
                headers = mapOf(
                    "Content-Type" to "application/json"
                )
            )
        }
    }

    fun createNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val request: NoteUpdateRequest = mapper.readValue(httpRequest.body, NoteUpdateRequest::class.java)
        val note = Note(
            id = UUID.randomUUID(),
            title = request.title,
            content = request.content,
        )
        // Don't check if it exists
        notesRepository.save(note)
        mapper.writeValue(responseBodyStream, note.toDto())
        return HttpResponseDetails(
            statusCode = HttpStatus.CREATED,
            headers = mapOf(
                "Content-Type" to "application/json"
            )
        )
    }

    fun deleteNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val note = notesRepository.findById(UUID.fromString(httpRequest.uri.queryParams["id"] ?: ""))
        return if (note == null) {
            HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        } else {
            notesRepository.delete(note.id)
            HttpResponseDetails(
                statusCode = HttpStatus.OK,
                headers = mapOf()
            )
        }
    }

    fun updateNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val request: NoteUpdateRequest = mapper.readValue(httpRequest.body, NoteUpdateRequest::class.java)
        val note = notesRepository.findById(UUID.fromString(httpRequest.uri.queryParams["id"] ?: ""))
        if (note == null) {
            return HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        }
        val updatedNote = note.copy(
            title = request.title,
            content = request.content,
        )
        notesRepository.save(updatedNote)
        mapper.writeValue(responseBodyStream, updatedNote.toDto())
        return HttpResponseDetails(
            statusCode = HttpStatus.OK,
            headers = mapOf()
        )
    }

    fun findCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val collection = collectionsRepository.findById(UUID.fromString(httpRequest.uri.queryParams["id"] ?: ""))
        return if (collection == null) {
            HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        } else {
            mapper.writeValue(responseBodyStream, collection.toDto())
            HttpResponseDetails(
                statusCode = HttpStatus.OK,
                headers = mapOf(
                    "Content-Type" to "application/json"
                )
            )
        }
    }

    fun createCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val request: NotesCollectionUpdateRequest = mapper.readValue(httpRequest.body, NotesCollectionUpdateRequest::class.java)
        val collection = NotesCollection(
            id = UUID.randomUUID(),
            title = request.title,
            notes = request.notes.mapNotNull { notesRepository.findById(it) },
        )
        collectionsRepository.save(collection)
        mapper.writeValue(responseBodyStream, collection.toDto())
        return HttpResponseDetails(
            statusCode = HttpStatus.CREATED,
            headers = mapOf(
                "Content-Type" to "application/json"
            )
        )
    }

    fun deleteCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val collection = collectionsRepository.findById(UUID.fromString(httpRequest.uri.queryParams["id"] ?: ""))
        return if (collection == null) {
            HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        } else {
            collectionsRepository.delete(collection.id)
            HttpResponseDetails(
                statusCode = HttpStatus.OK,
                headers = mapOf()
            )
        }
    }

    fun updateCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val request: NotesCollectionUpdateRequest = mapper.readValue(httpRequest.body, NotesCollectionUpdateRequest::class.java)
        val collection = collectionsRepository.findById(UUID.fromString(httpRequest.uri.queryParams["id"] ?: ""))
        if (collection == null) {
            return HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        }
        val updatedCollection = collection.copy(
            title = request.title,
            notes = request.notes.mapNotNull { notesRepository.findById(it) },
        )
        collectionsRepository.save(updatedCollection)
        mapper.writeValue(responseBodyStream, updatedCollection.toDto())
        return HttpResponseDetails(
            statusCode = HttpStatus.OK,
            headers = mapOf()
        )
    }
}
