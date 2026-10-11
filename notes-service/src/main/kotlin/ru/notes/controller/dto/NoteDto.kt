package ru.notes.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class NoteDto(
    @param:JsonProperty("id")
    val id: UUID,

    @param:JsonProperty("title")
    val title: String,

    @param:JsonProperty("content")
    val content: String,
)
