package org.khttp.server.handler.http2

import org.khttp.server.handler.FramePayload
import org.khttp.server.handler.Http2Frame

enum class StreamState {
    IDLE,
    RESERVED_LOCAL,
    RESERVED_REMOTE,
    OPEN,
    HALF_CLOSED_LOCAL,
    HALF_CLOSED_REMOTE,
    CLOSED,
}

class Http2Stream(
    val streamId: Int,
    var state: StreamState = StreamState.IDLE,
) {
    private val headersManager: HeadersManager = HeadersManager()

    fun handleClientHeaders(frame: Http2Frame) {
        headersManager.handleClientHeaders(frame.payload as FramePayload.HeadersPayload)
    }

    fun handleClientContinuation(frame: Http2Frame) {
        headersManager.handleClientContinuation(frame.payload as FramePayload.ContinuationPayload)
    }

    fun handleClientData(frame: Http2Frame) {

    }
}
