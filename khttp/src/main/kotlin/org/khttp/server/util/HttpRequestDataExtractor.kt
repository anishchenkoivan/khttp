package org.khttp.org.khttp.server.util

import org.khttp.org.khttp.http.HttpMethod
import org.khttp.org.khttp.http.HttpRequestInitialData
import org.khttp.org.khttp.http.HttpVersion
import java.io.InputStream
import java.util.Scanner

object HttpRequestDataExtractor {
    fun getRequestData(clientInputStream: InputStream): HttpRequestInitialData {
        val scanner = Scanner(clientInputStream)
        if (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val segments = line.split(' ')

            val method = HttpMethod.valueOf(segments[0])
            val uri = segments[1]

            // TODO: Add proper version detection
             val version = when {
                 segments.size < 3 -> HttpVersion.HTTP_0_9
                 else -> HttpVersion.valueOf(segments[2])
             }

            return HttpRequestInitialData(
                version,
                method,
                uri,
            )
        }
        // TODO: Add custom exceptions
        throw IllegalStateException("Invalid request data")
    }
}
