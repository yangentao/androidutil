package dev.entao.http

import java.io.IOException
import java.io.OutputStream

class SizeStream : OutputStream() {
    var size = 0
        private set

    @Throws(IOException::class)
    override fun write(oneByte: Int) {
        ++size
    }

    fun incSize(size: Int) {
        this.size += size
    }

}