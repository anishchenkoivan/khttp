package ru.notes.controller

import io.github.oshai.kotlinlogging.KotlinLogging
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
    private val logger = KotlinLogging.logger {}
    private val mapper = Json.mapper

    fun findNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val id = UUID.fromString(httpRequest.uri.queryParams["id"] ?: "")
        val note = notesRepository.findById(id)
        return if (note == null) {
            logger.warn { "Note with id=$id not found" }
            HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        } else {
            logger.info { "Found Note with id=$id" }
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
        logger.info { "Created Note with id=${note.id}" }
        mapper.writeValue(responseBodyStream, note.toDto())
        return HttpResponseDetails(
            statusCode = HttpStatus.CREATED,
            headers = mapOf(
                "Content-Type" to "application/json"
            )
        )
    }

    fun deleteNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val id = UUID.fromString(httpRequest.uri.queryParams["id"] ?: "")
        val note = notesRepository.findById(id)
        return if (note == null) {
            logger.warn { "Note with id=$id not found" }
            HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        } else {
            notesRepository.delete(note.id)
            logger.info { "Deleted Note with id=${note.id}" }
            HttpResponseDetails(
                statusCode = HttpStatus.OK,
                headers = mapOf()
            )
        }
    }

    fun updateNote(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val request: NoteUpdateRequest = mapper.readValue(httpRequest.body, NoteUpdateRequest::class.java)
        val id = UUID.fromString(httpRequest.uri.queryParams["id"] ?: "")
        val note = notesRepository.findById(id)
        if (note == null) {
            logger.warn { "Note with id=$id not found" }
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
        logger.info { "Updated Note with id=${updatedNote.id}" }
        mapper.writeValue(responseBodyStream, updatedNote.toDto())
        return HttpResponseDetails(
            statusCode = HttpStatus.OK,
            headers = mapOf()
        )
    }

    fun findCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val id = UUID.fromString(httpRequest.uri.queryParams["id"] ?: "")
        val collection = collectionsRepository.findById(id)
        return if (collection == null) {
            logger.warn { "Collection with id=$id not found" }
            HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        } else {
            logger.info { "Found Collection with id=$id" }
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
        logger.info { "Created Collection with id=${collection.id}" }
        mapper.writeValue(responseBodyStream, collection.toDto())
        return HttpResponseDetails(
            statusCode = HttpStatus.CREATED,
            headers = mapOf(
                "Content-Type" to "application/json"
            )
        )
    }

    fun deleteCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val id = UUID.fromString(httpRequest.uri.queryParams["id"] ?: "")
        val collection = collectionsRepository.findById(id)
        return if (collection == null) {
            logger.warn { "Collection with id=$id not found" }
            HttpResponseDetails(
                statusCode = HttpStatus.NOT_FOUND,
                headers = mapOf()
            )
        } else {
            collectionsRepository.delete(collection.id)
            logger.info { "Deleted Collection with id=${collection.id}" }
            HttpResponseDetails(
                statusCode = HttpStatus.OK,
                headers = mapOf()
            )
        }
    }

    fun updateCollection(httpRequest: HttpRequest, responseBodyStream: OutputStream): HttpResponseDetails {
        val request: NotesCollectionUpdateRequest = mapper.readValue(httpRequest.body, NotesCollectionUpdateRequest::class.java)
        val id = UUID.fromString(httpRequest.uri.queryParams["id"] ?: "")
        val collection = collectionsRepository.findById(id)
        if (collection == null) {
            logger.warn { "Collection with id=$id not found" }
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
        logger.info { "Updated Collection with id=${updatedCollection.id}" }
        mapper.writeValue(responseBodyStream, updatedCollection.toDto())
        return HttpResponseDetails(
            statusCode = HttpStatus.OK,
            headers = mapOf()
        )
    }
}
