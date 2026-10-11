package org.khttp.server.handler.http2

import org.khttp.server.handler.FramePayload

class HeadersManager {
    private var headersPayload: CharArray? = null

    fun handleClientHeaders(payload: FramePayload.HeadersPayload) {
        headersPayload = payload.headersPayload
    }

    fun handleClientContinuation(payload: FramePayload.ContinuationPayload) {
        headersPayload = headersPayload!! + payload.headersPayload
    }

    val clientHeaders: Map<Int, Map<String, String>> = mapOf()
}
