package org.khttp.org.khttp.server.writer

import org.khttp.org.khttp.http.response.HttpResponse
import java.io.OutputStream

class Http09ResponseWriter(): HttpResponseWriter {
    // TODO: Use piped streams
    override fun writeResponse(response: HttpResponse, outputStream: OutputStream) {
        outputStream.write(response.bodyStream.getData())
    }
}
