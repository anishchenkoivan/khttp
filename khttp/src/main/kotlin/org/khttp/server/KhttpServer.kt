package org.khttp.server

import io.github.oshai.kotlinlogging.KotlinLogging
import org.khttp.http.HttpVersion
import org.khttp.server.handler.Http09Handler
import org.khttp.server.handler.Http11Handler
import org.khttp.server.handler.Http20Handler
import org.khttp.server.handler.HttpRequestHandler
import org.khttp.server.util.HttpRequestDataExtractor
import org.khttp.server.util.QueryRouter
import java.net.ServerSocket
import java.net.Socket

private val logger = KotlinLogging.logger {}

class KhttpServer {
    private var isActive = false

    // TODO: Add associateBy
    private val handlerByVersion: Map<HttpVersion, HttpRequestHandler> = mapOf(
        HttpVersion.HTTP_0_9 to Http09Handler(),
        HttpVersion.HTTP_1_1 to Http11Handler(),
        HttpVersion.HTTP_2_0 to Http20Handler(),
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
        logger.info {"Handling client connection"}
        val clientInputStreamReader = clientSocket.getInputStream().bufferedReader()
        val clientOutputStream = clientSocket.getOutputStream()

        val initialRequestData = HttpRequestDataExtractor.getRequestData(clientInputStreamReader)
        handlerByVersion[initialRequestData.httpVersion]?.handleRequest(
            clientConnectionReader = clientInputStreamReader,
            clientOutputStream = clientOutputStream,
            initialData = initialRequestData,
            router = router,
        )

        clientSocket.close()
        clientOutputStream.close()
        clientInputStreamReader.close()
    }
}
