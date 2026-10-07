package org.khttp.org.khttp.server.handler

import org.khttp.org.khttp.http.HttpRequestInitialData
import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.server.util.QueryRouter
import java.io.BufferedReader
import java.io.OutputStream

interface HttpRequestHandler {
    val supportedVersion: HttpVersion
    fun handleRequest(
        clientConnectionReader: BufferedReader,
        clientOutputStream: OutputStream,
        initialData: HttpRequestInitialData,
        router: QueryRouter,
    )
}
