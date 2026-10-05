package org.khttp.org.khttp.http

enum class HttpVersion(val value: String) {
    HTTP_0_9("HTTP/0.9"),
    HTTP_1_1("HTTP/1.1"),
    HTTP_2_0("HTTP/2.0"),
    HTTP_3_0("HTTP/3.0"),
}

fun httpVersionFromString(s: String): HttpVersion? = HttpVersion.entries.firstOrNull { it.value == s }
