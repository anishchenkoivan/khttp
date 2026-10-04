package org.khttp.org.khttp.server

import io.github.oshai.kotlinlogging.KotlinLogging
import org.khttp.org.khttp.http.HttpVersion
import org.khttp.org.khttp.server.handler.Http09Handler
import org.khttp.org.khttp.server.handler.HttpRequestHandler
import org.khttp.org.khttp.server.util.HttpRequestDataExtractor
import org.khttp.org.khttp.server.util.QueryRouter
import java.net.ServerSocket
import java.net.Socket

private val logger = KotlinLogging.logger {}

class KhttpServer {
    private var isActive = false
    private val handlerByVersion: Map<HttpVersion, HttpRequestHandler> = mapOf(
        HttpVersion.HTTP_0_9 to Http09Handler()
    )
    val router: QueryRouter = QueryRouter()

    fun listen(tcpPort: Int) {
        val serverSocket = ServerSocket(tcpPort)
        isActive = true
        logger.info {"Server listening on port $tcpPort"}
        while (isActive) {
            val clientSocket = serverSocket.accept()
            handleClientConnection(clientSocket)
        }
    }

    fun stop() {
        isActive = false
    }

    fun handleClientConnection(clientSocket: Socket) {
        val clientInputStream = clientSocket.getInputStream()
        val clientOutputStream = clientSocket.getOutputStream()

        val initialRequestData = HttpRequestDataExtractor.getRequestData(clientInputStream)
        handlerByVersion[initialRequestData.httpVersion]?.handleRequest(
            clientConnectionStream = clientInputStream,
            clientOutputStream = clientOutputStream,
            initialData = initialRequestData,
            router = router,
        )

        clientInputStream.close()
        clientOutputStream.close()
        clientSocket.close()
    }
}
