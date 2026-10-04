package org.khttp.org.khttp.http.response

import org.khttp.org.khttp.http.HttpStatus
import org.khttp.org.khttp.stream.HttpResponseStream

data class HttpResponse (
    val statusCode: HttpStatus,
    val headers: Map<String, String>,
    val bodyStream: HttpResponseStream,
)
