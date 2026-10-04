package org.khttp.org.khttp.http

data class HttpRequestInitialData(
    val httpVersion: HttpVersion,
    val method: HttpMethod?,
    val uri: String?,
)
