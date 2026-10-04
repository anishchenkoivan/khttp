package org.khttp.org.khttp.http.request

import org.khttp.org.khttp.http.HttpMethod
import org.khttp.org.khttp.http.HttpUri
import java.io.InputStream

open class GenericHttpRequest(
    override val method: HttpMethod,
    override val uri: HttpUri,
    override val headers: Map<String, String>,
    override val body: InputStream
) : HttpRequest
