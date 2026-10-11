package org.khttp.http.response

import org.khttp.http.HttpStatus
import org.khttp.stream.HttpResponseStream

data class HttpResponseDetails(
    val statusCode: HttpStatus,
    val headers: Map<String, String>,
)

fun HttpResponseDetails.toHttpResponse(bodyStream: HttpResponseStream): HttpResponse {
    return HttpResponse(statusCode, headers, bodyStream)
}
