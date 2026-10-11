package org.khttp.http.request

import org.khttp.http.HttpMethod
import org.khttp.http.HttpUri
import java.io.InputStream

interface HttpRequest {
    val method: HttpMethod
    val uri: HttpUri
    val headers: Map<String, String>
    val body: String
}
