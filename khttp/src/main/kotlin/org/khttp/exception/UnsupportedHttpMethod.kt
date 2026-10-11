package org.khttp.exception

class UnsupportedHttpMethod(
    override val message: String?
): RuntimeException() {
}
