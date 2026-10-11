package org.khttp.http

enum class HttpMethod(val value: String) {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    PATCH("PATCH"),
    OPTIONS("OPTIONS"),
    CONNECT("CONNECT"),
    HEAD("HEAD"),
    TRACE("TRACE"),
    QUERY("QUERY"),
}
