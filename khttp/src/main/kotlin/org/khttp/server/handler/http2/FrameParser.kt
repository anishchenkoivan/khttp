package org.khttp.server.handler.http2

import org.khttp.server.handler.FramePayload
import org.khttp.server.handler.FrameType
import org.khttp.server.handler.Http2Frame
import java.io.BufferedReader

object FrameParser {
    fun parseFrame(reader: BufferedReader): Http2Frame {
        val frameBuffer = CharArray(9)
        reader.read(frameBuffer)
        val length = (frameBuffer[0].code shl 16) or
                (frameBuffer[1].code shl 8) or
                frameBuffer[2].code

        val type = FrameType.entries.firstOrNull { it.code == frameBuffer[3] }
            ?: throw IllegalArgumentException("Unknown HTTP/2 frame type: ${frameBuffer[3].code}")

        val flags = BooleanArray(8) { index -> ((frameBuffer[4].code shr index) and 1) == 1 }

        val streamId = ((frameBuffer[5].code and 0x7F) shl 24) or
                (frameBuffer[6].code shl 16) or
                (frameBuffer[7].code shl 8) or
                frameBuffer[8].code

        val payloadBuffer = CharArray(length)
        reader.read(payloadBuffer)
        val payload: FramePayload = when (type) {
            FrameType.SETTINGS -> parseSettingsPayload(payloadBuffer)
            FrameType.HEADERS -> parseHeadersPayload(payloadBuffer, flags)
            FrameType.DATA -> parseDataPayload(payloadBuffer)
            FrameType.CONTINUATION -> parseContinuationPayload(payloadBuffer)
            else -> FramePayload.CharArrayPayload(payloadBuffer)
        }

        return Http2Frame(
            length = length,
            type = type,
            flags = flags,
            streamId = streamId,
            payload = payload,
        )
    }

    private fun parseSettingsPayload(inputData: CharArray): FramePayload.SettingsPayload {
        TODO("Not yet implemented")
    }

    private fun parseHeadersPayload(inputData: CharArray, flags: BooleanArray): FramePayload.HeadersPayload {
        var offset = 0
        val padded = flags[3]
        val priority = flags[5]

        val padLength: Short? = if (padded) inputData[offset++].code.toShort() else null

        var exclusiveDependency: Boolean? = null
        var streamDependency: Int? = null
        var weight: Short? = null
        if (priority) {
            val value = (inputData[offset++].code shl 24) or
                    (inputData[offset++].code shl 16) or
                    (inputData[offset++].code shl 8) or
                    inputData[offset++].code
            exclusiveDependency = ((value ushr 31) and 1) == 1
            streamDependency = value and 0x7FFFFFFF
            weight = inputData[offset++].code.toShort()
        }

        val headerEnd = inputData.size - (padLength?.toInt() ?: 0)
        val headersPayload = inputData.copyOfRange(offset, headerEnd)

        return FramePayload.HeadersPayload(
            padLength = padLength,
            exclusiveDependency = exclusiveDependency,
            streamDependency = streamDependency,
            weight = weight,
            headersPayload = headersPayload,
        )
    }

    private fun parseDataPayload(inputData: CharArray): FramePayload.DataPayload {
        TODO("Not yet implemented")
    }

    private fun parseContinuationPayload(inputData: CharArray): FramePayload.ContinuationPayload {
        TODO("Not yet implemented")
    }
}
