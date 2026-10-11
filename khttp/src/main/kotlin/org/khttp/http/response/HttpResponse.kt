package org.khttp.http.response

import org.khttp.http.HttpStatus
import org.khttp.stream.HttpResponseStream

data class HttpResponse (
    val statusCode: HttpStatus,
    val headers: Map<String, String>,
    val bodyStream: HttpResponseStream,
)
