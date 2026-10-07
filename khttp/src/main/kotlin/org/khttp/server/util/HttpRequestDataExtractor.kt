package org.khttp.org.khttp.server.util

import org.khttp.org.khttp.http.HttpMethod
import org.khttp.org.khttp.http.HttpRequestInitialData
import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.http.httpVersionFromString
import java.io.BufferedReader

object HttpRequestDataExtractor {
    fun getRequestData(inputStreamReader: BufferedReader): HttpRequestInitialData {
        val line = getInitialString(inputStreamReader)
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

    private fun getInitialString(inputStreamReader: BufferedReader): String {
        val line = inputStreamReader.readLine()
        return if (line.startsWith(HTTP_2_0_MAGIC_INITIAL)) {
            val http2TailResult = inputStreamReader.read(shortBuffer)
            if (http2TailResult != -1) {
                if (shortBuffer.concatToString().contentEquals(HTTP_2_0_MAGIC_TAIL)) {
                    HTTP_2_0_MAGIC
                }
            }
            line
        } else {
            line
        }
    }

    private const val HTTP_2_0_MAGIC_INITIAL = "PRI * HTTP/2.0"
    private const val HTTP_2_0_MAGIC_TAIL = "PRI * HTTP/2.0"
    private const val HTTP_2_0_MAGIC = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n"

    private val shortBuffer = CharArray(10)
}
