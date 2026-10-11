package ru.notes

import org.khttp.http.HttpMethod
import org.khttp.http.HttpStatus
import org.khttp.http.request.HttpRequest
import org.khttp.http.response.HttpResponseDetails
import org.khttp.server.KhttpServer
import java.io.OutputStream

fun main() {
    val server = KhttpServer()
    server.router
        .route(
            path = "/ping",
            method = HttpMethod.GET,
            worker = { httpRequest: HttpRequest, responseBodyStream: OutputStream ->
                val body = httpRequest.body
                responseBodyStream.write("Success\nBody:\n".toByteArray())
                responseBodyStream.write(body.toByteArray())
                responseBodyStream.write("\nQuery Params:\n".toByteArray())
                responseBodyStream.write(httpRequest.uri.queryParams.toString().toByteArray())
                responseBodyStream.write("\nFragment:\n".toByteArray())
                responseBodyStream.write((httpRequest.uri.fragment?:"").toByteArray())
                HttpResponseDetails(HttpStatus.OK, mapOf())
            }
        )
    server.listen(8081)
}
