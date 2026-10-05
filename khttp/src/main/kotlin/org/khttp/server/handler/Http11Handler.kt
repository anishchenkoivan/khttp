package org.khttp.org.khttp.server.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.khttp.org.khttp.http.HttpRequestInitialData
import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.http.request.KHttpRequest
import org.khttp.org.khttp.http.response.toHttpResponse
import org.khttp.org.khttp.server.util.HttpUriParser
import org.khttp.org.khttp.server.util.QueryRouter
import org.khttp.org.khttp.server.util.SocketParser
import org.khttp.org.khttp.server.writer.Http11ResponseWriter
import org.khttp.org.khttp.stream.HttpResponseStream
import java.io.OutputStream

private val logger = KotlinLogging.logger {}

class Http11Handler: HttpRequestHandler {
    override val supportedVersion: HttpVersion = HttpVersion.HTTP_1_1

    private val responseWriter = Http11ResponseWriter()

    override fun handleRequest(
        clientConnectionStreamParser: SocketParser,
        clientOutputStream: OutputStream,
        initialData: HttpRequestInitialData,
        router: QueryRouter
    ) {
        logger.info { "Handling HTTP/1.1 request" }
        val uriData = HttpUriParser.parse(initialData.uri!!)
        val headers = parseHeaders(clientConnectionStreamParser)
        val contentLength = headers["Content-Length"]?.toInt()
        val httpRequest = KHttpRequest(
            method = initialData.method!!,
            uri = uriData,
            headers = headers,
            body = parseBody(clientConnectionStreamParser, contentLength)
        )

        val responseStream = HttpResponseStream()
        val response = router.resolveRoute(uriData.path, initialData.method)
            .handle(httpRequest, responseStream)
            .toHttpResponse(responseStream)

        responseWriter.writeResponse(response, responseStream)
    }

    private fun parseHeaders(parser: SocketParser): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        val headersSection = if (parser.hasNextSection()) {
            parser.nextSection()
        } else { "" }

        if (headersSection.isNotEmpty()) {
            headersSection.split("\n").forEach { line ->
                if (line.isBlank()) return@forEach
                val kv = line.split(":", limit=2)
                headers[kv[0].trim()] = kv[1].trim()
            }
        }

        return headers
    }

    fun parseBody(parser: SocketParser, size: Int?): String {
        if (size == null || size == 0) return ""
        return if (parser.hasNextSection()) {
            parser.nextSection()
        } else { "" }
    }
}
