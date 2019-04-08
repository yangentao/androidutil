@file:Suppress("unused")

package dev.entao.base

import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

const val UTF8 = "UTF-8"
private const val PROGRESS_DELAY = 50

typealias BlockUnit = ()->Unit

fun <T : Closeable> T?.closeSafe() {
    try {
        this?.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

@Throws(IOException::class)
fun copyStream(input: InputStream, closeIs: Boolean, os: OutputStream, closeOs: Boolean, total: Int, progress: Progress?) {
    try {
        progress?.onProgressStart(total)

        val buf = ByteArray(4096)
        var pre = System.currentTimeMillis()
        var recv = 0

        var n = input.read(buf)
        while (n != -1) {
            os.write(buf, 0, n)
            recv += n
            if (progress != null) {
                val curr = System.currentTimeMillis()
                if (curr - pre > PROGRESS_DELAY) {
                    pre = curr
                    progress.onProgress(recv, total, if (total > 0) recv * 100 / total else 0)
                }
            }
            n = input.read(buf)
        }
        os.flush()
        progress?.onProgress(recv, total, if (total > 0) recv * 100 / total else 0)
    } finally {
        if (closeIs) {
            input.closeSafe()
        }
        if (closeOs) {
            os.closeSafe()
        }
        progress?.onProgressFinish()
    }
}