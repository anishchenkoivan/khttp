package org.khttp.server.handler

data class Http2Frame(
    val length: Int,
    val type: FrameType,
    val flags: BooleanArray,
    val streamId: Int,
    val payload: FramePayload,
) {

}

enum class FrameType(val code: Char) {
    DATA('\u0000'),
    HEADERS('\u0001'),
    PRIORITY('\u0002'),
    RST_STREAM('\u0003'),
    SETTINGS('\u0004'),
    PUSH_PROMISE('\u0005'),
    PING('\u0006'),
    GOAWAY('\u0007'),
    WINDOW_UPDATE('\u0008'),
    CONTINUATION('\u0009'),
}

sealed class FramePayload {
    data class SettingsPayload(
        val entries: List<SettingEntry>
    ) : FramePayload()

    data class HeadersPayload(
        // If PADDED is set
        val padLength: Short?,
        // If PRIORITY is set
        val exclusiveDependency: Boolean?,
        val streamDependency: Int?,
        val weight: Short?,

        val headersPayload: CharArray,
    ) : FramePayload()
    data class ContinuationPayload(
        val headersPayload: CharArray
    ) : FramePayload()
    data class DataPayload(
        // TODO: Replace with actual values
        val somePayload: CharArray,
    ) : FramePayload()
    class CharArrayPayload(val content: CharArray) : FramePayload()
}

data class SettingEntry(val identifier: Short, val value: Int)
data class HeaderEntry(val index: Int, val name: String, val value: String) : FramePayload()
