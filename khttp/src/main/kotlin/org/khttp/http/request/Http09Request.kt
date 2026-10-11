package org.khttp.http.request

import org.khttp.http.HttpMethod
import org.khttp.http.HttpUri
import java.io.InputStream

data class Http09Request(
    override val uri: HttpUri
): KHttpRequest(
    uri = uri,
    method = HttpMethod.GET,
    headers = emptyMap(),
    body = ""
)
