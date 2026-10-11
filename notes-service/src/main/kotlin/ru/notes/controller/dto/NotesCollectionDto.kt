package ru.notes.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class NotesCollectionDto(
    @param:JsonProperty("id")
    val id: UUID,

    @param:JsonProperty("title")
    val title: String,

    @param:JsonProperty("notes")
    val notes: List<NoteDto>,
)
