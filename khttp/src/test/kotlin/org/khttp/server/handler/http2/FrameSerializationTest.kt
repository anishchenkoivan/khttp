package org.khttp.server.handler.http2

import org.khttp.server.handler.FramePayload
import org.khttp.server.handler.FrameType
import org.khttp.server.handler.Http2Frame
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class FrameSerializationTest {
    @Test
    fun headers() {
        val flags = BooleanArray(8)
        flags[3] = true // PADDED
        flags[5] = true // PRIORITY

        val headersPayload = ByteArray(20)
        Random.nextBytes(headersPayload)

        val padLength = (0..255).random().toShort()
        val weight = (0..255).random().toShort()

        val inputHeadersFrame = Http2Frame(
            length = 1 + 4 + 1 + headersPayload.size + padLength,
            type = FrameType.HEADERS,
            flags = flags,
            streamId = Random.nextInt() and Int.MAX_VALUE,
            payload = FramePayload.HeadersPayload(
                padLength = padLength,
                exclusiveDependency = false,
                streamDependency = Random.nextInt() and Int.MAX_VALUE,
                weight = weight,
                headersPayload = headersPayload.map { (it.toInt() and 0xFF).toChar() }.toCharArray(),
            ),
        )

        val serializedFrame = FrameWriter.writeFrame(inputHeadersFrame)
        val parsedFrame = FrameParser.parseFrame(
            BufferedReader(InputStreamReader(ByteArrayInputStream(serializedFrame), Charsets.ISO_8859_1)),
        )

        assertEquals(inputHeadersFrame.length, parsedFrame.length)
        assertEquals(inputHeadersFrame.type, parsedFrame.type)
        assertContentEquals(inputHeadersFrame.flags, parsedFrame.flags)
        assertEquals(inputHeadersFrame.streamId, parsedFrame.streamId)

        val originalPayload = inputHeadersFrame.payload as FramePayload.HeadersPayload
        val parsedPayload = parsedFrame.payload as FramePayload.HeadersPayload
        assertEquals(originalPayload.padLength, parsedPayload.padLength)
        assertEquals(originalPayload.exclusiveDependency, parsedPayload.exclusiveDependency)
        assertEquals(originalPayload.streamDependency, parsedPayload.streamDependency)
        assertEquals(originalPayload.weight, parsedPayload.weight)
        assertContentEquals(originalPayload.headersPayload, parsedPayload.headersPayload)
    }
}