package ru.notes.controller

import org.khttp.http.HttpMethod
import org.khttp.http.HttpStatus
import org.khttp.http.request.HttpRequest
import org.khttp.http.response.HttpResponseDetails
import org.khttp.server.util.QueryRouter
import java.io.OutputStream

fun route(controller: NotesController, router: QueryRouter) {
    router.route(
        path = "/notes",
        method = HttpMethod.GET,
        worker = controller::findNote
    )
    router.route(
        path = "/notes",
        method = HttpMethod.POST,
        worker = controller::createNote
    )
    router.route(
        path = "/notes",
        method = HttpMethod.DELETE,
        worker = controller::deleteNote
    )
    router.route(
        path = "/notes",
        method = HttpMethod.PUT,
        worker = controller::updateNote
    )
    router.route(
        path = "/collections",
        method = HttpMethod.GET,
        worker = controller::findCollection
    )
    router.route(
        path = "/collections",
        method = HttpMethod.POST,
        worker = controller::createCollection
    )
    router.route(
        path = "/collections",
        method = HttpMethod.DELETE,
        worker = controller::deleteCollection
    )
    router.route(
        path = "/collections",
        method = HttpMethod.PUT,
        worker = controller::updateCollection
    )
    router.route(
        path = "/ping",
        method = HttpMethod.GET,
        worker = { httpRequest: HttpRequest, responseBodyStream: OutputStream ->
            val body = httpRequest.body
            responseBodyStream.write("Success\nBody:\n".toByteArray())
            responseBodyStream.write(body.toByteArray())
            responseBodyStream.write("\nQuery Params:\n".toByteArray())
            responseBodyStream.write(httpRequest.uri.queryParams.toString().toByteArray())
            responseBodyStream.write("\nFragment:\n".toByteArray())
            responseBodyStream.write((httpRequest.uri.fragment ?: "").toByteArray())
            HttpResponseDetails(HttpStatus.OK, mapOf())
        }
    )
}