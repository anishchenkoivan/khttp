package ru.notes

import org.khttp.org.khttp.server.KhttpServer

fun main() {
    val server = KhttpServer()
    server.listen(8080)
}
