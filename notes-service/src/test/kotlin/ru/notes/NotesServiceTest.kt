package ru.notes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ru.notes.controller.dto.Json
import ru.notes.controller.dto.NoteDto
import ru.notes.controller.dto.NoteUpdateRequest
import ru.notes.controller.dto.NotesCollectionDto
import ru.notes.controller.dto.NotesCollectionUpdateRequest
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class NotesServiceTest {

    @Test
    fun `should create and find the note`() {
        val createdResponse = post("/notes", NoteUpdateRequest("Note title", "Note content"))
        assertEquals(201, createdResponse.statusCode())
        val note = createdResponse.bodyAsNote()

        val found = get("/notes?id=${note.id}")
        assertEquals(200, found.statusCode())
        assertEquals(note, found.bodyAsNote())
    }

    @Test
    fun `should update the note`() {
        val note = post("/notes", NoteUpdateRequest("Note title", "Note content")).bodyAsNote()

        val updated = NoteDto(note.id, "Updated note title", "Updated note content")
        assertEquals(200, put("/notes?id=${note.id}", NoteUpdateRequest("Updated note title", "Updated note content")).statusCode())

        val found = get("/notes?id=${note.id}")
        assertEquals(updated, found.bodyAsNote())
    }

    @Test
    fun `should delete the note`() {
        val note = post("/notes", NoteUpdateRequest("Note title", "Note content")).bodyAsNote()

        assertEquals(200, delete("/notes?id=${note.id}").statusCode())
        assertEquals(404, get("/notes?id=${note.id}").statusCode())
    }

    @Test
    fun `should create and find the collection`() {
        val note = post("/notes", NoteUpdateRequest("Note title", "Note content")).bodyAsNote()

        val createdResponse = post("/collections", NotesCollectionUpdateRequest("Collection title", listOf(note.id)))
        assertEquals(201, createdResponse.statusCode())
        val collection = createdResponse.bodyAsCollection()

        val found = get("/collections?id=${collection.id}")
        assertEquals(200, found.statusCode())
        assertEquals(collection, found.bodyAsCollection())
    }

    @Test
    fun `should update the collection`() {
        val note = post("/notes", NoteUpdateRequest("Note title", "Note content")).bodyAsNote()

        val collection = post("/collections", NotesCollectionUpdateRequest("Collection title", emptyList())).bodyAsCollection()

        val update = NotesCollectionUpdateRequest("Updated collection title", listOf(note.id))
        assertEquals(200, put("/collections?id=${collection.id}", update).statusCode())

        val found = get("/collections?id=${collection.id}").bodyAsCollection()
        assertEquals("Updated collection title", found.title)
        assertEquals(listOf(note), found.notes)
    }

    @Test
    fun `should delete the collection`() {
        val collection = post("/collections", NotesCollectionUpdateRequest("Collection title", emptyList())).bodyAsCollection()

        assertEquals(200, delete("/collections?id=${collection.id}").statusCode())
        assertEquals(404, get("/collections?id=${collection.id}").statusCode())
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun startServer() {
            Thread { main() }.apply { isDaemon = true }.start()
            waitUntilServerIsUp()
        }
    }
}

private val httpClient: HttpClient = HttpClient.newHttpClient()
private const val BASE_URL = "http://localhost:8081"

private fun waitUntilServerIsUp() {
    val deadline = System.currentTimeMillis() + 10_000
    var lastFailure: Exception? = null
    while (System.currentTimeMillis() < deadline) {
        try {
            if (get("/ping").statusCode() == 200) return
        } catch (e: Exception) {
            lastFailure = e
        }
        Thread.sleep(50)
    }
    throw IllegalStateException("Server did not start within 10 seconds", lastFailure)
}

private fun get(path: String): HttpResponse<String> =
    httpClient.send(
        HttpRequest.newBuilder(URI.create("$BASE_URL$path")).GET().build(),
        HttpResponse.BodyHandlers.ofString(),
    )

private fun post(path: String, body: Any): HttpResponse<String> = send("POST", path, body)

private fun put(path: String, body: Any): HttpResponse<String> = send("PUT", path, body)

private fun delete(path: String): HttpResponse<String> =
    httpClient.send(
        HttpRequest.newBuilder(URI.create("$BASE_URL$path")).DELETE().build(),
        HttpResponse.BodyHandlers.ofString(),
    )

private fun send(method: String, path: String, body: Any): HttpResponse<String> =
    httpClient.send(
        HttpRequest.newBuilder(URI.create("$BASE_URL$path"))
            .header("Content-Type", "application/json")
            .method(method, HttpRequest.BodyPublishers.ofString(Json.mapper.writeValueAsString(body)))
            .build(),
        HttpResponse.BodyHandlers.ofString(),
    )

private fun HttpResponse<String>.bodyAsNote(): NoteDto =
    Json.mapper.readValue(body(), NoteDto::class.java)

private fun HttpResponse<String>.bodyAsCollection(): NotesCollectionDto =
    Json.mapper.readValue(body(), NotesCollectionDto::class.java)
