package org.khttp.org.khttp.server.writer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.http.response.HttpResponse
import java.io.OutputStream

val logger = KotlinLogging.logger {}

class Http11ResponseWriter: HttpResponseWriter {
    override fun writeResponse(
        response: HttpResponse,
        outputStream: OutputStream
    ) {
        logger.info { "Writing response" }
        // TODO: Support chunked encoding
        val bodyBytes = response.bodyStream.getData()
        val outputStringBuilder = StringBuilder()
        outputStringBuilder.append("${HttpVersion.HTTP_1_1.value} ${response.statusCode.code} ${response.statusCode.name}\r\n")
        outputStringBuilder.append(String(headersToByteArray(response.headers)))
        outputStringBuilder.append("Connection: close\r\n")
        outputStringBuilder.append("Content-Type: text/plain; charset=utf-8\r\n")
        outputStringBuilder.append("Content-Length: ${bodyBytes.size}\r\n")
        outputStringBuilder.append("\r\n")
        outputStringBuilder.append(String(bodyBytes))
        val outputString = outputStringBuilder.toString()
        println("OUTPUT STRING:\n---\n$outputString\n---")
        outputStream.write(outputStringBuilder.toString().toByteArray())
        outputStream.flush()
    }

    private fun headersToByteArray(headers: Map<String, String>): ByteArray {
        return headers.entries.joinToString("\r\n") { "${it.key}: ${it.value}" }.toByteArray()
    }
}
