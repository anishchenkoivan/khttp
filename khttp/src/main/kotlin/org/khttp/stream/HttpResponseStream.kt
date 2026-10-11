package org.khttp.stream

import java.io.OutputStream

class HttpResponseStream: OutputStream() {
    private val buffer = mutableListOf<Byte>()

    override fun write(b: Int) {
        buffer.add(b.toByte())
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        buffer.addAll(b.slice(off until off + len))
    }

    fun getData(): ByteArray {
        return buffer.toByteArray()
    }
}
