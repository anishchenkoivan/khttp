package ru.notes

import org.khttp.org.khttp.http.HttpMethod
import org.khttp.org.khttp.http.HttpStatus
import org.khttp.org.khttp.http.request.HttpRequest
import org.khttp.org.khttp.http.response.HttpResponseDetails
import org.khttp.org.khttp.server.KhttpServer
import java.io.OutputStream

fun main() {
    val server = KhttpServer()
    server.router
        .route(
            path = "/ping",
            method = HttpMethod.GET,
            worker = { httpRequest: HttpRequest, responseBodyStream: OutputStream ->
                val body = httpRequest.body.readAllBytes()
                responseBodyStream.write("Success\nBody:\n".toByteArray())
                responseBodyStream.write(body)
                responseBodyStream.write("\nQuery Params:\n".toByteArray())
                responseBodyStream.write(httpRequest.uri.queryParams.toString().toByteArray())
                responseBodyStream.write("\nFragment:\n".toByteArray())
                responseBodyStream.write((httpRequest.uri.fragment?:"").toByteArray())
                HttpResponseDetails(HttpStatus.OK, mapOf())
            }
        )
    server.listen(8080)
}
