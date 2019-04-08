package dev.entao.base

import java.io.Closeable

val UTF8 = "UTF-8"

fun <T : Closeable> T?.closeSafe() {
    try {
        this?.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}