package org.khttp.org.khttp.server.util

import org.khttp.org.khttp.handler.HttpWorker
import org.khttp.org.khttp.http.HttpMethod

private data class RoutingKey(
    val path: String,
    val method: HttpMethod,
)

class QueryRouter {
    private val routingTable = mutableMapOf<RoutingKey, HttpWorker>()

    fun route(
        path: String,
        method: HttpMethod,
        worker: HttpWorker,
    ): QueryRouter {
        routingTable[RoutingKey(path, method)] = worker
        return this
    }

    fun resolveRoute(
        path: String,
        method: HttpMethod
    ): HttpWorker = routingTable[RoutingKey(path, method)]
        ?: throw IllegalArgumentException("No route found for $path $method")
}
