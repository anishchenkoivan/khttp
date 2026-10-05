package org.khttp.org.khttp.server.util

import org.khttp.org.khttp.http.HttpMethod
import org.khttp.org.khttp.http.HttpRequestInitialData
import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.http.httpVersionFromString

object HttpRequestDataExtractor {
    fun getRequestData(clientInputStreamParser: SocketParser): HttpRequestInitialData {
        if (clientInputStreamParser.hasNextSection()) {
            val line = clientInputStreamParser.nextSection()
            val segments = line.split(' ')

            val method = HttpMethod.valueOf(segments[0])
            val uri = segments[1]

            // TODO: Add proper version detection
             val version = when {
                 segments.size < 3 -> HttpVersion.HTTP_0_9
                  else -> httpVersionFromString(segments[2])!!
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
