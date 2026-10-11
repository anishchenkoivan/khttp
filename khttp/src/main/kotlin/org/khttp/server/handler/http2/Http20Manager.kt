package org.khttp.server.handler.http2

import org.khttp.server.handler.FramePayload
import org.khttp.server.handler.FrameType
import org.khttp.server.handler.Http2Frame
import org.khttp.server.util.QueryRouter
import java.io.BufferedReader
import java.io.OutputStream

class Http20Manager(
    val clientConnectionReader: BufferedReader,
    val clientOutputStream: OutputStream,
    val router: QueryRouter
) {
    private val streams: MutableMap<Int, Http2Stream> = mutableMapOf()
    private val connectionSettings = ConnectionSettings()

    fun handleRequest() {
        while (true) {
            val clientFrame = FrameParser.parseFrame(clientConnectionReader)
            when (clientFrame.type) {
                FrameType.SETTINGS -> handleSettingsFrame(clientFrame)
                FrameType.HEADERS -> handleHeadersFrame(clientFrame)
                FrameType.CONTINUATION -> handleContinuationFrame(clientFrame)
                FrameType.DATA -> handleDataFrame(clientFrame)
                else -> handleUnknownFrame(clientFrame)
            }
        }
    }

    fun writeFrame(frame: Http2Frame) {
        clientOutputStream.write(FrameWriter.writeFrame(frame))
        clientOutputStream.flush()
    }

    private fun handleSettingsFrame(frame: Http2Frame) {
        val ack = frame.flags[0]
        if (ack && connectionSettings.serverSettingsWritten) {
            connectionSettings.serverSettingsAcknowledged = true
            return
        }

        connectionSettings.clientSettings = frame.payload as FramePayload.SettingsPayload

        if (!connectionSettings.serverSettingsWritten) {
            val serverSettings = Http2Frame(
                length = 0,
                type = FrameType.SETTINGS,
                flags = BooleanArray(8),
                streamId = 0,
                payload = FramePayload.SettingsPayload(emptyList())
            )
            writeFrame(serverSettings)
            connectionSettings.serverSettingsWritten = true
        }

        val flags = BooleanArray(8)
        flags[0] = true
        val acknowledgedFrame = Http2Frame(
            length = 0,
            type = FrameType.HEADERS,
            flags = flags,
            streamId = 0,
            payload = FramePayload.SettingsPayload(emptyList())
        )
        writeFrame(acknowledgedFrame)
    }

    private fun handleHeadersFrame(frame: Http2Frame) {
        val streamId = frame.streamId
        val stream = Http2Stream(
            streamId = streamId,
        )
        streams[streamId] = stream
        stream.handleClientHeaders(frame)
    }

    private fun handleContinuationFrame(frame: Http2Frame) {
        val streamId = frame.streamId
        streams[streamId]!!.handleClientContinuation(frame)
    }

    private fun handleDataFrame(frame: Http2Frame) {
    }

    private fun handleUnknownFrame(frame: Http2Frame) {}
}

private class ConnectionSettings{
    var clientSettings: FramePayload.SettingsPayload? = null
    var serverSettingsWritten = false
    var serverSettingsAcknowledged = false
}
