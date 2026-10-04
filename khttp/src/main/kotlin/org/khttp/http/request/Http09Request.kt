package org.khttp.org.khttp.http.request

import org.khttp.org.khttp.http.HttpMethod
import org.khttp.org.khttp.http.HttpUri
import java.io.InputStream

data class Http09Request(
    override val uri: HttpUri
): GenericHttpRequest(
    uri = uri,
    method = HttpMethod.GET,
    headers = emptyMap(),
    body = InputStream.nullInputStream()
)
