package org.khttp.org.khttp.server.handler

import org.khttp.org.khttp.exception.UnsupportedHttpMethod
import org.khttp.org.khttp.http.HttpMethod
import org.khttp.org.khttp.http.HttpRequestInitialData
import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.http.request.Http09Request
import org.khttp.org.khttp.http.response.toHttpResponse
import org.khttp.org.khttp.server.util.HttpUriParser
import org.khttp.org.khttp.server.util.QueryRouter
import org.khttp.org.khttp.server.writer.Http09ResponseWriter
import org.khttp.org.khttp.stream.HttpResponseStream
import java.io.InputStream
import java.io.OutputStream

class Http09Handler: HttpRequestHandler {
    override val supportedVersion: HttpVersion = HttpVersion.HTTP_0_9

    private val responseWriter = Http09ResponseWriter()

    override fun handleRequest(
        clientConnectionStream: InputStream,
        clientOutputStream: OutputStream,
        initialData: HttpRequestInitialData,
        router: QueryRouter
    ) {
        if (initialData.method != HttpMethod.GET) {
            throw UnsupportedHttpMethod("HTTP/0.9 only supports GET method, got ${initialData.method} instead")
        }
        val uriData = HttpUriParser.parse(initialData.uri!!)
        val httpRequest = Http09Request(uriData)
        val responseStream = HttpResponseStream()
        val response = router.resolveRoute(uriData.path, initialData.method)
            .handle(httpRequest, responseStream)
            .toHttpResponse(responseStream)

        responseWriter.writeResponse(response, clientOutputStream)
    }
}
