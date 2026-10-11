package org.khttp.handler

import org.khttp.http.request.HttpRequest
import org.khttp.http.response.HttpResponseDetails
import java.io.OutputStream

fun interface HttpWorker {
    fun handle(
        httpRequest: HttpRequest,
        responseBodyStream: OutputStream,
    ): HttpResponseDetails
}
