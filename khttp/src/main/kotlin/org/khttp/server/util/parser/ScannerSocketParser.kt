package org.khttp.org.khttp.server.util.parser

import org.khttp.org.khttp.server.util.SocketParser
import java.io.InputStream
import java.util.Scanner

class ScannerSocketParser(stream: InputStream): SocketParser {
    private enum class SegmentType {
        QUERY,
        HEADERS,
        BODY,
    }
    private val scanner = Scanner(stream)
    private var segment: String? = null

    private val segmentOrder = listOf(
        SegmentType.QUERY,
        SegmentType.HEADERS,
        SegmentType.BODY,
    )
    private var segmentIndex = 0

    override fun hasNextSection(): Boolean {
        if (segment == null) {
            parseNextSegment()
        }
        return segment != null
    }

    override fun nextSection(): String {
        val requestedSegment = segment ?: throw IllegalStateException("No more segments available")
        segment = null
        return requestedSegment
    }

    private fun parseNextSegment() {
        val segmentType = segmentOrder[segmentIndex]
        when (segmentType) {
            SegmentType.QUERY -> {
                parseNextShortSegment()
                segmentIndex++
            }
            SegmentType.HEADERS -> {
                parseHeadersSegment()
                segmentIndex++
            }
            SegmentType.BODY -> parseHeadersSegment()
        }
    }

    private fun parseNextShortSegment() {
        if (scanner.hasNextLine()) {
            segment = scanner.nextLine()
        }
    }

    private fun parseHeadersSegment() {
        val segmentBuilder = StringBuilder()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            segmentBuilder.appendLine(line)
            if (line.isNullOrBlank()) {
                break
            }
        }
        segment = segmentBuilder.toString()
    }
}
