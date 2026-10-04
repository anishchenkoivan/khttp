package org.khttp.org.khttp.server

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.OutputStream
import java.net.ServerSocket
import java.util.Scanner

private val logger = KotlinLogging.logger {}

class KhttpServerOld {
    fun listen(port: Int) {
        val serverSocket = ServerSocket(port)
        logger.info { "Server listening on port $port" }
        val clientSocket = serverSocket.accept()
        val clientInputStream = clientSocket.getInputStream()
        val clientOutputStream = clientSocket.getOutputStream()

        val scanner = Scanner(clientInputStream)
        logger.debug { "Receiving request data" }
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            println { line }
            if (line.isNullOrEmpty()) {
                break
            }
        }

        logger.debug { "Sending response" }
        respond(clientOutputStream)

        clientSocket.close()
        serverSocket.close()
    }

    private fun respond(outputStream: OutputStream) {
        val body = "hehe\n".toByteArray(Charsets.UTF_8)
        val head = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/plain; charset=utf-8\r\n" +
            "Content-Length: ${body.size}\r\n" +
            "Connection: close\r\n" +
            "\r\n"
        outputStream.write(head.toByteArray(Charsets.ISO_8859_1))
        outputStream.write(body)
        outputStream.flush()
    }
}
