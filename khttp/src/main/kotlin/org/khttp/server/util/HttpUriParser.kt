package org.khttp.server.util

import org.khttp.http.HttpUri
import kotlin.math.min

object HttpUriParser {
    fun parse(uri: String): HttpUri {
        val fragmentDelimiterIndex = uri.indexOf("#").takeIf { it != -1 } ?: uri.length
        val queryParamsDelimiterIndex = uri.indexOf("?").takeIf { it != -1 } ?: fragmentDelimiterIndex

        val fragment = uri.substring(fragmentDelimiterIndex)
        val path = uri.substring(0, queryParamsDelimiterIndex)
        val queryParams: Map<String, String> = uri
            .substring(min(queryParamsDelimiterIndex + 1, fragmentDelimiterIndex), fragmentDelimiterIndex)
            .takeIf{ it.isNotEmpty() }
            ?.split("&")
            ?.mapNotNull { param ->
                val parts = param.split("=", limit = 2)
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            ?.toMap() ?: emptyMap()

        return HttpUri(
            path,
            queryParams,
            fragment
        )
    }
}
