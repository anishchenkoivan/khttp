package org.khttp.org.khttp.server.handler

import org.khttp.org.khttp.http.HttpRequestInitialData
import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.server.util.QueryRouter
import org.khttp.org.khttp.server.util.SocketParser
import java.io.OutputStream

interface HttpRequestHandler {
    val supportedVersion: HttpVersion
    fun handleRequest(
        clientConnectionStreamParser: SocketParser,
        clientOutputStream: OutputStream,
        initialData: HttpRequestInitialData,
        router: QueryRouter,
    )
}
