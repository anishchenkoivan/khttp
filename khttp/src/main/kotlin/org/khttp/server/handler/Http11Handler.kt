package org.khttp.server.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.khttp.http.HttpRequestInitialData
import org.khttp.http.HttpVersion
import org.khttp.http.request.KHttpRequest
import org.khttp.http.response.toHttpResponse
import org.khttp.server.util.HttpUriParser
import org.khttp.server.util.QueryRouter
import org.khttp.server.writer.Http11ResponseWriter
import org.khttp.stream.HttpResponseStream
import java.io.BufferedReader
import java.io.OutputStream

private val logger = KotlinLogging.logger {}

class Http11Handler: HttpRequestHandler {
    override val supportedVersion: HttpVersion = HttpVersion.HTTP_1_1

    private val responseWriter = Http11ResponseWriter()

    override fun handleRequest(
        clientConnectionReader: BufferedReader,
        clientOutputStream: OutputStream,
        initialData: HttpRequestInitialData,
        router: QueryRouter
    ) {
        logger.info { "Handling HTTP/1.1 request" }
        val uriData = HttpUriParser.parse(initialData.uri!!)
        val headers = getHeaders(clientConnectionReader)
        val chunkedEncoding = headers["Transfer-Encoding"]?.equals("chunked") ?: false
        val contentLength = headers["Content-Length"]?.toInt()
        val httpRequest = KHttpRequest(
            method = initialData.method!!,
            uri = uriData,
            headers = headers,
            body = if (!chunkedEncoding) {
                parseStaticBody(clientConnectionReader, contentLength)
            } else {
                parseChunkedBody(clientConnectionReader)
            }
        )

        val responseStream = HttpResponseStream()
        val response = router.resolveRoute(uriData.path, initialData.method)
            .handle(httpRequest, responseStream)
            .toHttpResponse(responseStream)

        responseWriter.writeResponse(response, clientOutputStream)
    }

    private fun getHeaders(reader: BufferedReader): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        val headersSection = parseHeaders(reader)

        if (headersSection.isNotEmpty()) {
            headersSection.split("\n").forEach { line ->
                if (line.isBlank()) return@forEach
                val kv = line.split(":", limit=2)
                headers[kv[0].trim()] = kv[1].trim()
            }
        }

        return headers
    }

    private fun parseHeaders(clientConnectionReader: BufferedReader): String {
        val headersLine = StringBuilder()
        while (true) {
            val line = clientConnectionReader.readLine()
            if (line.isEmpty()) break
            headersLine.appendLine(line)
        }
        return headersLine.toString()
    }

    private fun parseChunkedBody(reader: BufferedReader): String {
        val body = StringBuilder()
        while (true) {
            val chunkSize = reader.readLine().substringBefore(';').trim().toInt(16)
            if (chunkSize == 0) {
                while (reader.readLine().isNotEmpty()) {
                    continue
                }
                break
            }
            val buffer = CharArray(chunkSize)
            var read = 0
            while (read < chunkSize) {
                read += reader.read(buffer, read, chunkSize - read)
            }
            body.append(buffer)
            reader.readLine()
        }
        return body.toString()
    }

    fun parseStaticBody(reader: BufferedReader, size: Int?): String {
        val buffer = CharArray(size ?: 0)
        reader.read(buffer)
        return String(buffer)
    }
}
