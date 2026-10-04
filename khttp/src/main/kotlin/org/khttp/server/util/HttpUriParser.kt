package org.khttp.org.khttp.server.util

import org.khttp.org.khttp.http.HttpUri

object HttpUriParser {
    fun parse(uri: String): HttpUri {
        val fragmentDelimiterIndex = uri.indexOf("#").takeIf { it != -1 } ?: uri.length
        val queryParamsDelimiterInidex = uri.indexOf("?").takeIf { it != -1 } ?: fragmentDelimiterIndex

        val fragment = uri.substring(fragmentDelimiterIndex)
        val path = uri.substring(0, queryParamsDelimiterInidex)
        val queryParams: Map<String, String> = uri.substring(queryParamsDelimiterInidex, fragmentDelimiterIndex)
            .split("&")
            .map { it.split("=") }
            .associate { (key, value) -> key to value }

        return HttpUri(
            path,
            queryParams,
            fragment
        )
    }
}
