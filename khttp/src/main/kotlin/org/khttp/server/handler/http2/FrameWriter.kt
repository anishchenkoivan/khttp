package org.khttp.server.handler.http2

import org.khttp.server.handler.FramePayload
import org.khttp.server.handler.FrameType
import org.khttp.server.handler.Http2Frame

object FrameWriter {
    fun writeFrame(frame: Http2Frame): ByteArray {
        val payload = when (frame.type) {
            FrameType.HEADERS -> writeHeadersPayload(frame.flags, frame.payload as FramePayload.HeadersPayload)
            else -> TODO("Not yet implemented")
        }

        val buffer = ByteArray(9 + payload.size)
        buffer[0] = (payload.size ushr 16).toByte()
        buffer[1] = (payload.size ushr 8).toByte()
        buffer[2] = payload.size.toByte()
        buffer[3] = frame.type.code.code.toByte()
        buffer[4] = frame.flags.foldIndexed(0) { index, acc, flag ->
            acc or (if (flag) 1 shl index else 0)
        }.toByte()
        val streamId = frame.streamId and 0x7FFFFFFF
        buffer[5] = (streamId ushr 24).toByte()
        buffer[6] = (streamId ushr 16).toByte()
        buffer[7] = (streamId ushr 8).toByte()
        buffer[8] = streamId.toByte()
        payload.copyInto(buffer, 9)
        return buffer
    }

    private fun writeHeadersPayload(flags: BooleanArray, payload: FramePayload.HeadersPayload): ByteArray {
        val padded = flags[3]
        val priority = flags[5]
        val padLength = payload.padLength?.toInt() ?: 0

        val bytes = ArrayList<Byte>()
        if (padded) {
            bytes.add(padLength.toByte())
        }
        if (priority) {
            val dependency = payload.streamDependency ?: 0
            val exclusive = if (payload.exclusiveDependency == true) 1 else 0
            val value = (exclusive shl 31) or (dependency and 0x7FFFFFFF)
            bytes.add((value ushr 24).toByte())
            bytes.add((value ushr 16).toByte())
            bytes.add((value ushr 8).toByte())
            bytes.add(value.toByte())
            bytes.add((payload.weight ?: 0).toByte())
        }
        for (char in payload.headersPayload) {
            bytes.add((char.code and 0xFF).toByte())
        }
        if (padded) {
            repeat(padLength) { bytes.add(0) }
        }
        return bytes.toByteArray()
    }
}   