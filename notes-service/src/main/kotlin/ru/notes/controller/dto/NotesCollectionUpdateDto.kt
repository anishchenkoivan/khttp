package ru.notes.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class NotesCollectionUpdateDto(
    @param:JsonProperty("title")
    val title: String,

    @param:JsonProperty("notes")
    val notes: List<UUID>,
)
