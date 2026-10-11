package org.khttp.server.writer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.khttp.http.HttpVersion
import org.khttp.http.response.HttpResponse
import java.io.OutputStream

private val logger = KotlinLogging.logger {}

class Http11ResponseWriter: HttpResponseWriter {
    override fun writeResponse(
        response: HttpResponse,
        outputStream: OutputStream
    ) {
        logger.info { "Writing response" }
        // TODO: Support chunked encoding
        val bodyBytes = response.bodyStream.getData()
        val headers = withDefaultHeaders(response.headers, bodyBytes.size)

        val writer = outputStream.bufferedWriter()
        writer.write("${HttpVersion.HTTP_1_1.value} ${response.statusCode.code} ${response.statusCode.name}\r\n")
        headers.forEach { (name, value) ->
            writer.write("$name: $value\r\n")
        }
        writer.write("\r\n")
        writer.flush()

        outputStream.write(bodyBytes)
        outputStream.flush()
    }

    private fun withDefaultHeaders(headers: Map<String, String>, bodySize: Int): Map<String, String> {
        val result = headers.toMutableMap()
        result.setHeaderIfAbsent("Content-Length", bodySize.toString())
        result.setHeaderIfAbsent("Content-Type", "text/plain; charset=utf-8")
        result.setHeaderIfAbsent("Connection", "close")
        return result
    }

    private fun MutableMap<String, String>.setHeaderIfAbsent(key: String, value: String) {
        if (keys.none { it.equals(key, ignoreCase = true) }) {
            this[key] = value
        }
    }
}
