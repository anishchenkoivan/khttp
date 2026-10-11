package ru.notes.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NoteUpdateRequest(
    @param:JsonProperty("title")
    val title: String,

    @param:JsonProperty("content")
    val content: String,
)
