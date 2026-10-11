package org.khttp.server.writer

import org.khttp.http.response.HttpResponse
import java.io.OutputStream

interface HttpResponseWriter {
    fun writeResponse(response: HttpResponse, outputStream: OutputStream)
}
