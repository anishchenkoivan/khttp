package org.khttp.org.khttp.http.request

import org.khttp.org.khttp.http.HttpMethod
import org.khttp.org.khttp.http.HttpUri
import java.io.InputStream

interface HttpRequest {
    val method: HttpMethod
    val uri: HttpUri
    val headers: Map<String, String>
    val body: InputStream
}
