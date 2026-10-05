package org.khttp.org.khttp.server.writer

import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.http.response.HttpResponse
import java.io.OutputStream

class Http11ResponseWriter: HttpResponseWriter {
    override fun writeResponse(
        response: HttpResponse,
        outputStream: OutputStream
    ) {
        outputStream.write("${HttpVersion.HTTP_1_1.name} ${response.statusCode.code} ${response.statusCode.name}\r\n".toByteArray())
        outputStream.write(headersToByteArray(response.headers))
        outputStream.write("\r\n".toByteArray())
        outputStream.write(response.bodyStream.getData())
        outputStream.write("\r\n".toByteArray())
    }

    private fun headersToByteArray(headers: Map<String, String>): ByteArray {
        return headers.entries.joinToString("\r\n") { "${it.key}: ${it.value}" }.toByteArray()
    }
}
