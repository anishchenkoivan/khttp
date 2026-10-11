package org.khttp.http

data class HttpUri(
    val path: String,
    val queryParams: Map<String, String> = emptyMap(),
    val fragment: String? = null,
)
