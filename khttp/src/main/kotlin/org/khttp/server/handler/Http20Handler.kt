package org.khttp.server.handler

import org.khttp.http.HttpRequestInitialData
import org.khttp.http.HttpVersion
import org.khttp.server.handler.http2.Http20Manager
import org.khttp.server.util.QueryRouter
import java.io.BufferedReader
import java.io.OutputStream

class Http20Handler: HttpRequestHandler {
    override val supportedVersion: HttpVersion = HttpVersion.HTTP_2_0

    override fun handleRequest(
        clientConnectionReader: BufferedReader,
        clientOutputStream: OutputStream,
        initialData: HttpRequestInitialData,
        router: QueryRouter
    ) {
        val manager = Http20Manager(
            clientConnectionReader,
            clientOutputStream,
            router,
        )

        manager.handleRequest()
    }
}
