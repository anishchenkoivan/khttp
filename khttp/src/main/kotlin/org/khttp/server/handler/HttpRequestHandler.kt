package org.khttp.server.handler

import org.khttp.http.HttpRequestInitialData
import org.khttp.http.HttpVersion
import org.khttp.server.util.QueryRouter
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
