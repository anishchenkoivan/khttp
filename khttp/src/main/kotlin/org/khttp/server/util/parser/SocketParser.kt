package org.khttp.org.khttp.server.util

interface SocketParser {
    fun hasNextSection(): Boolean
    fun nextSection(): String
}
